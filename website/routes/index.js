const express = require('express');
const {serverLog, errorLog, routeLog} = require('../logs/log');
const {getUserOptions} = require('../functions/helpers');
const passport = require('passport');
const https = require('https');
const fs = require('fs');

const router = express.Router();

var firebase = require("firebase");
var config = {
    apiKey: process.env.apiKey,
    authDomain: process.env.authDomain,
    databaseURL: process.env.databaseURL,
    projectId: process.env.projectId,
    storageBucket: process.env.storageBucket,
    messagingSenderId: process.env.messagingSenderId
  };
firebase.initializeApp(config);

console.log()



router.all('/*', (req, res, next) => {
    req.app.locals.layout = 'main';
    next();
});

router.get('/', (req, res) => {

    var ref = firebase.database().ref("/").once('value').then(function(snapshot) {

        const device1 = snapshot.val().devices['outlet-one'];
        const device2 = snapshot.val().devices['outlet-two'];
        const deviceNames = snapshot.val()['device-names'];

        if (device1.status.isOn === "true") {
            device1.status.isOn = true;
        } else { device1.status.isOn = false; }

        if (device2.status.isOn === "true") {
            device2.status.isOn = true;
        } else { device2.status.isOn = false; }

        res.render('pages/homepage', {deviceNames: deviceNames, device1: device1, device2: device2});
      });
});

router.get('/update/:device/:time/:watts', (req, res) => {
    const device = req.params.device;
    const time = req.params.time;
    const watts = req.params.watts;

    res.send(`Device: ${device}, Time: ${time}, Watts: ${watts}`);
});

router.get('/status/:device/:isOn', (req, res) => {
    const device = req.params.device;
    const status = req.params.isOn;

    // Reading the output file
    var lock = fs.readFileSync('kidLock.txt', 'utf8');

    if (lock === "true") {
        res.set('Content-Type', 'application/json');
        return res.send({status: "Locked out"});
    }
    else {
        // Setting up the API call
        const options = {
            host:'https://us-central1-yhack2018-77c5f.cloudfunctions.net',
            port: 443,
            path: `setStatus?time=123&device=${device}&isOn=${status}`
        };

        // // Creating a callback for our API
        const callback = (response) => {
            var result = '';
            // When it gets a data chunk add it to the result
            response.on('data', (chunk) => {
                result += chunk;
            });

            // When all the data has been transferred
            response.on('end', () => {

                // Logging that we used the API for this route
                serverLog(`Update Status - Device: ${device} - Status: ${status}`);

                global.io.emit('updateStatus', device, status);

                // Passing the data back to the client so they can display the map
                res.set('Content-Type', 'application/json');
                res.send({status: "Worked"});
            });
                
        }

        // // Calling the Google Maps API
        https.request(`https://us-central1-yhack2018-77c5f.cloudfunctions.net/setStatus?time=123&device=${device}&isOn=${status}`, callback).end();
    }

    
});

// Route to forward from arduino to firebase function
router.get('/addData/:time/:device/:value', (req, res) => {
    const time = req.params.time;
    const device = req.params.device;
    const value = req.params.value;

    console.log(`Time: ${time}, Device: ${device}, Value: ${value}`);

    res.set('Content-Type', 'text/plain');
    res.send('Worked');
});




// Exporting the routes to be used in our server.js
module.exports = router;

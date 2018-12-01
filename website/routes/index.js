const express = require('express');
const {serverLog, errorLog, routeLog} = require('../logs/log');
const {getUserOptions} = require('../functions/helpers');
const passport = require('passport');

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
    routeLog(`${req.method} ${req.originalUrl}`);
    req.app.locals.layout = 'main';
    next();
});

router.get('/', (req, res) => {
    // console.log(firebase.database());

    var ref = firebase.database().ref("/devices").once('value').then(function(snapshot) {
        // console.log(snapshot.val());

        const device1 = snapshot.val()['outlet-one'];
        const device2 = snapshot.val()['outlet-two'];

        // console.log(device1);
        // console.log(device2);

        res.render('pages/homepage', {outlet1: device1.data, outlet2: device2.data});
        // ...
      });

    // console.log(ref);

    
});

router.get('/update/:device/:time/:watts', (req, res) => {
    const device = req.params.device;
    const time = req.params.time;
    const watts = req.params.watts;

    res.send(`Device: ${device}, Time: ${time}, Watts: ${watts}`);
});

router.get('/status/:device/:status', (req, res) => {
    const device = req.params.device;
    const status = req.params.status;

    console.log(`${device} - ${status}`);

    const result = {
        device: device,
        isOn: status
    };

    res.set('Content-Type', 'application/json');
    res.send(result);
});




// Exporting the routes to be used in our server.js
module.exports = router;

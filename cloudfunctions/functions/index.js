/******************************************************************************
 * Required Modules
 *****************************************************************************/
const functions = require('firebase-functions');
const admin = require('firebase-admin');

/******************************************************************************
 * Initialization
 *****************************************************************************/
admin.initializeApp();

/******************************************************************************
 * Constants
 *****************************************************************************/
const DEVICE = 'devices';
const DEVICE_NAMES = 'device-names';

/******************************************************************************
 * Useful Functions
 *****************************************************************************/
function workedResponse(res) {
    res.set('Content-Type', 'text/plain');
    return res.send('Worked');
}
function sendJSON(res, json) {
    res.set('Content-Type', 'application/json');
    return res.send(json);
}


/******************************************************************************
 * Data POSTing Routes
 *****************************************************************************/

/* Add Function - Add values to a device in the Firebase
Example:
https://us-central1-testing-8e4bf.cloudfunctions.net/addData?time=<TIME>&device=<DEVNAME>&value=<VALUE>
*/
exports.addData = functions.https.onRequest((req, res) => {
    // Getting the parameters from the route request
    const timestamp = req.query.time;
    const device = req.query.device;
    const value = req.query.value;

    const info = {
        'watts': value,
        'time': timestamp
    };

    // Nesting promises is bad, but it works 
    return admin.database().ref(`/${DEVICE}/${device}/data/list`).push(info).then(snapshot => {
        return admin.database().ref(`/${DEVICE}/${device}/data/latest`).update(info).then(snapshot => {
            return workedResponse(res);
        });
    });
});

/* Set Status Function - Set the status of a device
Example:
https://us-central1-testing-8e4bf.cloudfunctions.net/setStatus?time=<TIME>&device=<DEVNAME>&isOn=<STATUS>
*/
exports.setStatus = functions.https.onRequest((req, res) => {
    // Getting the parameters from the route request
    const timestamp = req.query.time;
    const device = req.query.device;
    const isOn = req.query.isOn;

    var info = {
        'time': timestamp,
        'isOn': isOn
    };

    return admin.database().ref(`/${DEVICE}/${device}/status`).update(info).then(snapshot => {
        return workedResponse(res);
    });
});

/******************************************************************************
 * Data GETing Routes
 *****************************************************************************/

/* Get Latest Wattage - Get the latest time & wattage from the device
Example:
https://us-central1-testing-8e4bf.cloudfunctions.net/getLatest?device=<DEVNAME>&
*/
exports.getLatest = functions.https.onRequest((req, res) => {
    // Getting the parameters from the route request
    const device = req.query.device;

    // Nesting promises is bad, but it works 
    return admin.database().ref(`/${DEVICE}/${device}/data/latest`).once('value').then(snapshot => {
        return sendJSON(res, snapshot.val());
    }).catch(err => {
        return res.send(err);
    });
});

/* Get Status - Get the current status of the device
Example:
https://us-central1-testing-8e4bf.cloudfunctions.net/getStatus?device=<DEVNAME>&
*/
exports.getStatus = functions.https.onRequest((req, res) => {
    // Getting the parameters from the route request
    const device = req.query.device;

    // Nesting promises is bad, but it works 
    return admin.database().ref(`/${DEVICE}/${device}/status`).once('value').then(snapshot => {
        return sendJSON(res, snapshot.val());
    }).catch(err => {
        return res.send(err);
    });
});

/* Get Devices - Get the currently installed devices
Example:
https://us-central1-testing-8e4bf.cloudfunctions.net/getDevices
*/
exports.getDevices = functions.https.onRequest((req, res) => {
    // Nesting promises is bad, but it works 
    return admin.database().ref(`/${DEVICE}`).once('value').then(snapshot => {
        return sendJSON(res, snapshot.val());
    }).catch(err => {
        return res.send(err);
    });
});

/* Get Devices - Get the currently installed devices
Example:
https://us-central1-testing-8e4bf.cloudfunctions.net/getDeviceNames
*/
exports.getDeviceNames = functions.https.onRequest((req, res) => {
    // Nesting promises is bad, but it works 
    return admin.database().ref(`/${DEVICE_NAMES}`).once('value').then(snapshot => {
        return sendJSON(res, snapshot.val());
    }).catch(err => {
        return res.send(err);
    });
});



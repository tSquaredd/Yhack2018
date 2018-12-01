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
function errorResponse(res) {
    res.set('Content-Type', 'text/plain');
    return res.send('Error');
}
function sendJSON(res, json) {
    res.set('Content-Type', 'application/json');
    return res.send(json);
}
function logError(err) {
    console.log(err);
}


/******************************************************************************
 * Data ADDing Routes
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

    console.log(`addData() - Time: ${timestamp}, Device: ${device}, Value: ${value}`);

    const info = {
        'watts': value,
        'time': timestamp
    };

    // Nesting promises is bad, but it works 

    // Pushing the info into the array of timestamp/watts values
    return admin.database().ref(`/${DEVICE}/${device}/data/list`).push(info).then(snapshot => {

        // Updating the latest value inside the device
        return admin.database().ref(`/${DEVICE}/${device}/data/latest`).update(info).then(snapshot => {

            // Reading the current values from the database for this device
            return admin.database().ref(`/${DEVICE}/${device}/totals`).once('value').then(snapshot => {

                total = parseFloat(value) + parseFloat(snapshot.val().watts);

                // Updating the device's total wattage used
                return admin.database().ref(`/${DEVICE}/${device}/totals`).update({watts: total}).then(snapshot => {
                    return workedResponse(res);
                });

            }).catch(err => {
                logError(err);
                return errorResponse(res);
            });
        });
    });
});

/******************************************************************************
 * Data UPDATEing Routes
 *****************************************************************************/

/* Set Status Function - Set the status of a device
Example:
https://us-central1-testing-8e4bf.cloudfunctions.net/setStatus?time=<TIME>&device=<DEVNAME>&isOn=<STATUS>
*/
exports.setStatus = functions.https.onRequest((req, res) => {
    // Getting the parameters from the route request
    const timestamp = req.query.time;
    const device = req.query.device;
    const isOn = req.query.isOn;

    console.log(`setStatus() - Time: ${timestamp}, Device: ${device}, isOn: ${isOn}`);

    var info = {
        'time': timestamp,
        'isOn': isOn
    };

    return admin.database().ref(`/${DEVICE}/${device}/status`).update(info).then(snapshot => {
        return workedResponse(res);
    });
});

/******************************************************************************
 * Data READing Routes
 *****************************************************************************/

/* Get Latest Wattage - Get the latest time & wattage from the device
Example:
https://us-central1-testing-8e4bf.cloudfunctions.net/getLatest?device=<DEVNAME>&
*/
exports.getLatest = functions.https.onRequest((req, res) => {
    // Getting the parameters from the route request
    const device = req.query.device;

    console.log(`getLatest() - Device: ${device}`);

    // Nesting promises is bad, but it works 
    return admin.database().ref(`/${DEVICE}/${device}/data/latest`).once('value').then(snapshot => {
        return sendJSON(res, snapshot.val());
    }).catch(err => {
        logError(err);
        return errorResponse(res);
    });
});

/* Get Status - Get the current status of the device
Example:
https://us-central1-testing-8e4bf.cloudfunctions.net/getStatus?device=<DEVNAME>&
*/
exports.getStatus = functions.https.onRequest((req, res) => {
    // Getting the parameters from the route request
    const device = req.query.device;

    console.log(`getStatus() - Device: ${device}`)

    // Nesting promises is bad, but it works 
    return admin.database().ref(`/${DEVICE}/${device}/status`).once('value').then(snapshot => {
        return sendJSON(res, snapshot.val());
    }).catch(err => {
        logError(err);
        return errorResponse(res);
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
        logError(err);
        return errorResponse(res);
    });
});

/* Get Device Names - Get the names of all devices available
Example:
https://us-central1-testing-8e4bf.cloudfunctions.net/getDeviceNames
*/
exports.getDeviceNames = functions.https.onRequest((req, res) => {
    // Nesting promises is bad, but it works 
    return admin.database().ref(`/${DEVICE_NAMES}`).once('value').then(snapshot => {
        return sendJSON(res, snapshot.val());
    }).catch(err => {
        logError(err);
        return errorResponse(res);
    });
});

/* Get Device Total Usage - Gets the total usage of a single device
Example:
https://us-central1-testing-8e4bf.cloudfunctions.net/getDeviceNames
*/
exports.getDeviceTotalUsage = functions.https.onRequest((req, res) => {
    const device = req.params.device;

    // Nesting promises is bad, but it works 
    return admin.database().ref(`/${DEVICE}/${device}/totals`).once('value').then(snapshot => {

        return sendJSON(res, snapshot.val());
    }).catch(err => {
        logError(err);
        return errorResponse(res);
    });
});



/* Get Home Usage - Gets the current household total lifetime usage
Example:
https://us-central1-testing-8e4bf.cloudfunctions.net/getDeviceNames
*/
exports.getHomeUsage = functions.https.onRequest((req, res) => {
    // Nesting promises is bad, but it works 
    return admin.database().ref(`/${DEVICE}`).once('value').then(snapshot => {
        const query = snapshot.val();

        console.log(query);

        const outletOneTotal = parseFloat(query['outlet-one'].totals.watts);
        const outletTwoTotal = parseFloat(query['outlet-two'].totals.watts);

        const total = outletOneTotal + outletTwoTotal;

        return sendJSON(res, {watts: total});
    }).catch(err => {
        logError(err);
        return errorResponse(res);
    });
});

/* Get Home Usage - Gets the current household total lifetime usage
Example:
https://us-central1-testing-8e4bf.cloudfunctions.net/getDeviceNames
*/
exports.getCurrentHomeUsage = functions.https.onRequest((req, res) => {
    // Nesting promises is bad, but it works 
    return admin.database().ref(`/${DEVICE}`).once('value').then(snapshot => {
        const query = snapshot.val();

        console.log(query);

        const outletOneTotal = parseFloat(query['outlet-one'].data.latest.watts);
        const outletTwoTotal = parseFloat(query['outlet-two'].data.latest.watts);

        const total = outletOneTotal + outletTwoTotal;

        return sendJSON(res, {watts: total});
    }).catch(err => {
        logError(err);
        return errorResponse(res);
    });
});



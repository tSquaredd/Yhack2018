# Smartlet


## Firebase Cloud Function Routes

| Function            | Params              | Route                                                                      |
|---------------------|---------------------|----------------------------------------------------------------------------|
| addData             | time, device, value | https://us-central1-yhack2018-77c5f.cloudfunctions.net/addData             |
| setStatus           | time, device, isOn  | https://us-central1-yhack2018-77c5f.cloudfunctions.net/setStatus           |
| getLatest           | device              | https://us-central1-yhack2018-77c5f.cloudfunctions.net/getLatest           |
| getStatus           | device              | https://us-central1-yhack2018-77c5f.cloudfunctions.net/getStatus           |
| getDevices          |                     | https://us-central1-yhack2018-77c5f.cloudfunctions.net/getDevices          |
| getDeviceNames      |                     | https://us-central1-yhack2018-77c5f.cloudfunctions.net/getDeviceNames      |
| getHomeUsage        |                     | https://us-central1-yhack2018-77c5f.cloudfunctions.net/getHomeUsage        |
| getCurrentHomeUsage |                     | https://us-central1-yhack2018-77c5f.cloudfunctions.net/getCurrentHomeUsage |
| getDeviceTotalUsage | device              | https://us-central1-yhack2018-77c5f.cloudfunctions.net/getDeviceTotalUsage |
| getCarbonAnalysis   | OPTIONAL:device     | https://us-central1-yhack2018-77c5f.cloudfunctions.net/getCarbonAnalysis   |
| resetDatabase       | password            | https://us-central1-yhack2018-77c5f.cloudfunctions.net/resetDatabase       |

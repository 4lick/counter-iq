# counter-iq

### Generating a Key Using OpenSSL

```go
> openssl genrsa -out developer_key.pem 4096
> openssl pkcs8 -topk8 -inform PEM -outform DER -in key.pem -out key.der -nocrypt
```

### Launch Android Emulator
```go
/Path/to/Android/sdk/tools/emulator -netdelay none -netspeed full -avd Nexus_5X_API_23  
```

### Install Garmin Connect Mobile
```go
> adb install Garmin\ Connect™\ Mobile_v3.8.0.1_apkpure.com.apk
```

### Run Connect-IQ App
```go
> cd counter-widget
> ./run.sh
```

###  Allow the simulator to communicate over ADB
```go
> adb forward tcp:7381 tcp:7381
```

Watch Counter App - connect iq / android


<p>
  <img src="/doc/counter-widget.png" align="left" height="200" width="150">
  <img src="/doc/counter-android.png" align="left" width="150">
</p>

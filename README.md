# GateTIK
A multiplatform application for parking gate control and parking monitoring.

### Tested Platforms
| Tested On | Android | macOS | Windows | Linux |
|-----------|---------|-------|--------|-------|
| Status    | ✅      | ✅     | ✅️      | ✅️    |

Status:
- ✅ = tested & works

### Role Support Matrix
| Role | Mobile | Desktop |
|------|---------|---------|
| **Admin** | ☑️     | ✅      |
| **Staff** | ☑️     | ✅      |
| **Student** | ✅   | ❌      |

Support Status:
- ✅ = full support
- ☑️ = partial support (some features unavailable)
- ❌ = not supported

## 📥 Installation
### 📄Set Up `local.properties`
Inside `<project root>/local.properties` file, add the following properties:
```properties
BACKEND_URL=<Backend base URL>
WEB_RTC_PLAYER_BASE_URL=<WebRTC Player Base URL>
ANDROID_INSTALLATION_URL=<Android APK Distribution URL>
VERSION=<Application Version>
LATEST_RELEASE_INFO_URL=<GitHub latest release info URL>
```

### 📱 Android
#### 🔹 Requirements
- Android Studio.
- Android device running **Android 7.0 (Nougat) or higher**.

#### 🔹 Debug Build
1. Clone the repository

    ```bash
    git clone https://github.com/kite1412/GateTIK.git
    ```
2. Open the downloaded repository in Android Studio.
3. Simply click run (▶️) button at the top of the Android Studio to install and launch the app.

#### 🔹 Release Build (preferred for smaller apk size)
1. Clone the repository

    ```bash
    git clone https://github.com/kite1412/GateTIK.git
    ```
2. Open the downloaded repository in Android Studio.
3. Navigate to **Build > Generate Signed APP Bundle / APK**.
4. Select **APK** to build signed APK.
5. Choose an existing `.jks` file or create a new one to sign the APK.
6. Select **release** as Build Variants then **Create**, wait for the APK file generation to complete.
7. In project's root directory, run the following command:

    ```bash
    adb install androidApp/release/androidApp-release.apk
    ```

### 🖥️ Desktop
#### 🔹 Requirements
- JDK 21

#### 🔹 Launch from Gradle
1. Clone the repository

    ```bash
    git clone https://github.com/kite1412/GateTIK.git
    ```
2. Set `JAVA_HOME` before starting Gradle:

   **macOS / Linux**
   ```bash
   export JAVA_HOME=<path to JDK 21>
   ```

   **Windows (PowerShell)**
   ```bash
   $env:JAVA_HOME = "<path to JDK 21>"
   ```
3. Run the following Gradle command:

   **macOS / Linux / Windows (PowerShell)**
   ```bash
   ./gradlew :desktopApp:run
   ```

#### 🔹 Release Package
1. Clone the repository

    ```bash
    git clone https://github.com/kite1412/GateTIK.git
   ```
2. Set `JAVA_HOME` before starting Gradle:

   **macOS / Linux**
   ```bash
   export JAVA_HOME=<path to JDK 21>
   ```

   **Windows (PowerShell)**
   ```bash
   $env:JAVA_HOME = "<path to JDK 21>"
   ```
3. Depending on the platform used for building the release package:

   **macOS**
   ```bash
   ./gradlew :desktopApp:packageReleaseDmg
   ```

   **Linux**
   ```bash
   ./gradlew :desktopApp:packageReleaseDeb
   ```

   **Windows (PowerShell)**
   ```bash
   ./gradlew :desktopApp:packageReleaseMsi
   ```
4. Install the application using the generated installer located in `desktopApp/build/compose/binaries/main-release/<installer type>`
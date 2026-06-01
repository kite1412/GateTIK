# PortalTIK
A multiplatform project for parking gate control and parking monitoring.

### Tested Platforms
| Tested On | Android | macOS | Windows | Linux |
|-----------|---------|-------|---------|-------|
| Status    | ✅      | ✅     | ⚠️      | ⚠️    |

Status:
- ✅ = tested & works
- ⚠️ = untested / expected to work

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
### 📱 Android
#### 🔹 Requirements
- Android Studio.
- Android device running **Android 7.0 (Nougat) or higher**.

#### 🔹 Debug Build
1. Clone the repository

    ```bash
    git clone https://github.com/kite1412/PortalTIK.git
    ```
2. Open the downloaded repository in Android Studio.
3. Simply click run (▶️) button at the top of the Android Studio to install and launch the app.

#### 🔹 Release Build (preferred for smaller apk size)
1. Clone the repository

    ```bash
    git clone https://github.com/kite1412/PortalTIK.git
    ```
2. Open the downloaded repository in Android Studio.
3. Navigate to **Build > Generate Signed APP Bundle / APK**.
4. Select **APK** to build signed APK.
5. Choose an existing `.jks` file or create a new one to sign the APK.
6. Select **release** as Build Variants then **Create**, wait for the APK file generation to complete.
7. In project's root directory, run the following command:

    ```bash
    adb install composeApp/release/composeApp-release.apk
    ```

### 🖥️ Desktop
#### 🔹 Requirements
- Android Studio / IntelliJ IDEA.

#### 🔹 Launch from Gradle
1. Clone the repository

    ```bash
    git clone https://github.com/kite1412/PortalTIK.git
    ```
2. Open the downloaded repository in Android Studio / IntelliJ IDEA.
3. Run the following gradle command:

   **macOs / Linux**
   ```bash
   ./gradlew :composeApp:run
   ```

   **Windows**
   ```bash
   gradlew.bat :composeApp:run
   ```

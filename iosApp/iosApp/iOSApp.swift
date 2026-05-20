import SwiftUI
import ComposeApp

@main
struct iOSApp: App {
    init() {
        Koin.shared.doInit()
    }
    
    var body: some Scene {
        WindowGroup {
            ContentView()
        }
    }
}

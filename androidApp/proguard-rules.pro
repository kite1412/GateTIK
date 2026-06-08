# VLC
-keep class org.videolan.** { *; }
-keep class org.vlc.** { *; }

-keepclasseswithmembernames class * {
    native <methods>;
}
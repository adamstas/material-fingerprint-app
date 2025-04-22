# MatTag

**Visual Material Analysis for Android**  

Discover the unique visual identity of materials through their perceptual fingerprints.

![MatTag Splash Screen](app/src/main/res/drawable/splash.png)

MatTag enables intuitive exploration and comparison of materials based on how we see and perceive them. 
Capture material appearances, analyze key visual attributes, and search your database — all with the power of smart visual encoding.

## Features

- **Dual Photo Capture with Format Enforcement**  
  Allows users to capture two photos of a material for analysis.

- **Server-Backed Material Analysis**  
  Communicates with a remote server to perform material analysis, retrieve stored materials and compute material similarities.

- **Interactive Polar Graphs**  
  Visualizes material characteristics via polar graphs — compare one or two materials using two curves in single plot or side-by-side graphs.

- **Anonymous Cloud Sync (Opt-In)**  
  Users can choose whether to anonymously sync analyzed data to the server or keep it local.

- **Persistent Local Storage**  
  Stores captured photos and analysis results locally for offline access.

- **Material Browser with Filtering**  
  Browse both local and remote materials, with filters by name and category.

- **Local Similarity Search**  
  Find visually similar materials within the device using an offline similarity algorithm.

- **Remote Similarity Search**  
  Search for similar materials from a central database stored on the server.

- **Visual Fingerprint Filtering (Remote)**  
  Filter server-stored materials by adjusting values for each of the 16 visual characteristics.

- **Visual Fingerprint Filtering (Local)**  
  Search locally stored materials using the same 16-dimensional visual fingerprint, all processed offline.

## Technical Requirements

- Android 12 (API level 31) or higher
- Functional camera
- Internet connection for material analysis and remotely stored materials filtering

## Installation

1. **Download** the APK from the TODO section.
2. **Enable** installation from unknown sources in your device settings.
3. **Open** the APK file to begin installation.
4. **Grant** camera permission when prompted.
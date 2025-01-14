# VortexManager

VortexManager is an innovative application designed to efficiently manage and process geospatial and environmental data. With powerful tools and a user-friendly interface, it is tailored for professionals and researchers working in geospatial analytics, environmental management, and related fields.

---

## 🚀 Features

- **Shapefile Import and Visualization**: Seamlessly import and render shapefiles with a robust visualization panel.
- **Interactive Map Controls**: Includes essential map navigation tools such as pan, zoom, full extent, and detailed information retrieval.
- **Customizable Toolbar**: Add or modify tools for specific tasks and workflows.
- **Extensibility**: Easily extend the application to include custom data processing and analysis functions.

---

## 🛠️ Tech Stack

- **Programming Language**: Java
- **Libraries**:
  - All Geometry modules developed using Java for admin spatial data objects. 
  - [GeoTools](https://geotools.org): 3rd party library only For shapefile data processing and visualization.
  - [Swing](https://docs.oracle.com/javase/tutorial/uiswing/): For the graphical user interface.
- **Build Tool**: Maven or Gradle (depending on your configuration)

---

---

## 🖥️ Setup & Installation

### Prerequisites
- Java 8 or higher
- Maven or Gradle
- Git

### Steps
1. Clone the repository:
   ```bash
   git clone https://github.com/your-username/VortexManager.git
   ```
2. Navigate to the project directory:
   ```bash
   cd VortexManager
   ```
3. Build the project using Maven:
   ```bash
   mvn clean install
   ```
4. Run the application:
   ```bash
   java -jar target/VortexManager.jar
   ```

---

## 🧩 Usage

1. **Load Shapefiles**: Use the "Import" button or menu to load a shapefile for visualization.
2. **Navigate the Map**: Utilize the toolbar for map navigation tools like pan, zoom in/out, and full extent.
3. **Inspect Features**: Click the "Info" button to retrieve detailed information about map features.

---


## 📝 License

This project is licensed under the [MIT License](LICENSE).

---



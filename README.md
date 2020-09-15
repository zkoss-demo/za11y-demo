# ZA11Y Demo

Demo project for a11y features and patterns since zk 9.5.0 (Using za11y.jar and the wcag/_c themes)

## Features demonstrated

* high contrast themes (wcag/wcag_c themes)
* landmarks
* basic / responsive layout
* based on commonly used ZK components
  * Layout: borderlayout, groubbox
  * Navigation: navbar, tabbox (multiple edit windows)
  * listbox, paging, filter, input elements
  * messagebox
* fully accessible via keyboard / screen reader
* ... (to be extended)

## Run/Build Commands

run jetty
```
./gradlew appRun
```

http://localhost:8080/za11y-demo

build war
```
./gradlew war
```

## License
* Demo Code - [Apache License 2.0](http://www.apache.org/licenses/LICENSE-2.0)
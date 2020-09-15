# ZA11Y (ZK-Accessibility) Demo

(_This demo is based on a preview version, feedback is welcome and will be considered in future improvements)._

A demo project for selected accessibility features and patterns since zk 9.5.0 (provided in
za11y.jar and the wcag/_c themes).

This demo serves as an introduction and doesn't demonstrate all available features / patterns.
For more details be sure to checkout [ZK Component Reference > Accessibility](https://www.zkoss.org/wiki/ZK_Component_Reference/Accessibility).

Studying the concepts about [WAI-ARIA](https://www.w3.org/TR/wai-aria/) 
and an understanding of the [WAI-ARIA Authoring Practices](https://www.w3.org/TR/wai-aria-practices/) 
are recommended, before designing and integrating an accessibility design in your application.   

## Features demonstrated

* high contrast themes (wcag/wcag_c themes)
* accessible via
  * keyboard (tab, arrow keys)
  * screen reader (nvda, voiceover, jaws, narrator)
  * mouse
  * touch
* basic / responsive layout
* landmarks ('banner', 'navigation', 'main')
* implemented with commonly used ZK components
  * Layout: borderlayout, groubbox
  * Navigation: navbar, tabbox (multiple edit windows)
  * listbox, paging, filter
  * input elements, buttons
  * messagebox
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
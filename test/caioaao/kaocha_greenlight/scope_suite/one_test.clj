(ns caioaao.kaocha-greenlight.scope-suite.one-test
  (:require
   [greenlight.step :as step]
   [greenlight.test :refer [deftest]]))

(deftest one-test
  "A sample greenlight test in the one test suite"
  #::step{:name   'noop-step
          :title  "Noop Step"
          :test   (constantly nil)})

(ns caioaao.kaocha-greenlight.scope-suite.two-test
  (:require
   [clojure.test :refer [is]]
   [greenlight.step :as step :refer [defstep]]
   [greenlight.test :as test :refer [deftest]]))

(deftest two-test
  "A sample greenlight test in the two test suite"
  #::step{:name   'noop-step
          :title  "Noop Step"
          :test   (constantly nil)})

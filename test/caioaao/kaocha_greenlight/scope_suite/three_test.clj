(ns caioaao.kaocha-greenlight.scope-suite.three-test
  (:require
   [clojure.test :refer [is]]
   [greenlight.step :as step :refer [defstep]]
   [greenlight.test :as test :refer [deftest]]))

(deftest three-test
  "A sample greenlight test in the three test suite"
  #::step{:name   'noop-step
          :title  "Noop Step"
          :test   (constantly nil)})

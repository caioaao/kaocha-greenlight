(ns caioaao.kaocha-greenlight.scope-suite.three-test
  (:require
   [greenlight.step :as step :refer [defstep]]
   [greenlight.test :as test :refer [deftest]]))

(defstep noop-step
  :title  "Noop Step"
  :test   (constantly nil))

(deftest three-test
  "A sample greenlight test in the three test suite"
  (noop-step))

(deftest another-three-test
  "Another sample greenlight test in the three test suite"
  (noop-step))

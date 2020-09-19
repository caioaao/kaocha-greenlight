(ns caioaao.kaocha-greenlight.scope-suite.two-test
  (:require
   [greenlight.step :as step :refer [defstep]]
   [greenlight.test :as test :refer [deftest]]))

(defstep noop-step
  :title  "Noop Step"
  :test   (constantly nil))

(deftest two-test
  "A sample greenlight test in the two test suite"
  (noop-step))

(deftest another-two-test
  "Another sample greenlight test in the two test suite"
  (noop-step))

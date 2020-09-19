(ns caioaao.kaocha-greenlight.scope-suite.one-test
  (:require
   [greenlight.step :as step :refer [defstep]]
   [greenlight.test :refer [deftest]]))

(defstep noop-step
  :title  "Noop Step"
  :test   (constantly nil))

(deftest one-test
  "A sample greenlight test in the one test suite"
  (noop-step))

(deftest another-one-test
  "Another sample greenlight test in the one test suite"
  (noop-step))

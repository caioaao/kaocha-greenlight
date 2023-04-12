(ns caioaao.kaocha-greenlight.timeout-suite.timeout-test
  (:require
   [clojure.test :refer [is]]
   [greenlight.step :as step]
   [greenlight.test :refer [deftest]]))

(deftest timeout-test
  "A sample greenlight test in the timeout test suite"
  #::step{:name    'timeout-step
          :title   "Timeout Step"
          :timeout 0
          :test    (fn [& _] (Thread/sleep 1000))})

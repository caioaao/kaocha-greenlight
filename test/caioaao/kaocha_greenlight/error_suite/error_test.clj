(ns caioaao.kaocha-greenlight.error-suite.error-test
  (:require
   [greenlight.step :as step]
   [greenlight.test :refer [deftest]]))

(deftest error-test
  "A sample greenlight test in the error test suite"
  #::step{:name   'error-step
          :title  "Error Step"
          :test   (fn [& _]
                    (throw (ex-info "oh no!" {:some "data"})))})

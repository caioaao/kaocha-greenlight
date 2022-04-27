(ns caioaao.kaocha-greenlight.test.var
  (:require
   [caioaao.kaocha-greenlight.runner :as runner]
   [clojure.spec.alpha :as s]
   [clojure.test :as ctest]
   [clojure.test :as t]
   [greenlight.report :as report]
   [greenlight.step :as step]
   [greenlight.test :as test]
   [kaocha.hierarchy :as hierarchy]
   [kaocha.testable :as testable]
   [kaocha.type.var]))

(defn ^:private run-test!
  [testable test-plan]
  (let [opts     {:print-color (:kaocha/color? test-plan)}
        report   (partial report/handle-test-event opts)
        system   (:caioaao.kaocha-greenlight.test/system test-plan)
        test-var (:kaocha.var/var testable)]
    (binding [test/*report* report]
      (test/run-test! system (test-var)))))

(defmethod testable/-run :caioaao.kaocha-greenlight.test/var
  [testable test-plan]
  (runner/run
   testable
   test-plan
   :var
   (fn [testable test-plan]
     (testable/-run
      (assoc testable
             ::testable/type :kaocha.type/var
             :kaocha.var/test (partial run-test! testable test-plan))
      test-plan))))

(s/def :caioaao.kaocha-greenlight.test/var
  (s/keys :req [::testable/type :kaocha.var/var]))

(hierarchy/derive! :caioaao.kaocha-greenlight.test/var
                   :kaocha.testable.type/leaf)

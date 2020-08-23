(ns caioaao.kaocha-greenlight.test.var
  (:require [kaocha.testable :as testable]
            [clojure.test :as ctest]
            [clojure.spec.alpha :as s]
            [kaocha.hierarchy :as hierarchy]
            [kaocha.report :as kaocha.report]
            [greenlight.test :as test]
            [kaocha.type]
            [greenlight.step :as step]))

(defn test-results->kaocha [rs]
  {:kaocha.result/pass    (:pass rs 0)
   :kaocha.result/error   (:error rs 0)
   :kaocha.result/fail    (:fail rs 0)
   :kaocha.result/pending (:pending rs 0)
   :kaocha.result/count   1})

(defn report [options event]
  (when (= :step-end (:type event))
    (->> (:step event)
         ::step/reports
         (run! ctest/do-report)))
  (ctest/do-report event))

(defmethod testable/-run :caioaao.kaocha-greenlight.test/var
  [{:caioaao.kaocha-greenlight.test/keys [test-var] :as testable}
   {:caioaao.kaocha-greenlight.test/keys [system] :as test-plan}]
  (ctest/do-report {:type :begin-test-var, :var test-var})
  (binding [ctest/*report-counters* (ref ctest/*initial-report-counters*)
            test/*report* (partial report {:print-color true})]
    (test/run-test! system (test-var))
    (ctest/do-report {:type :end-test-var, :var test-var})
    (merge testable (test-results->kaocha @ctest/*report-counters*))))

(s/def :caioaao.kaocha-greenlight.test/test-var var?)
(s/def :caioaao.kaocha-greenlight.test/var (s/keys :req [::testable/type :caioaao.kaocha-greenlight.test/test-var]))

(hierarchy/derive! :caioaao.kaocha-greenlight.test/var :kaocha.testable.type/leaf)

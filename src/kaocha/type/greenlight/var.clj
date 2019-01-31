(ns kaocha.type.greenlight.var
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

(defmethod testable/-run :kaocha.type/greenlight.var
  [{:keys [kaocha.greenlight/test-var] :as testable} {:keys [kaocha.greenlight/system] :as test-plan}]
  (ctest/do-report {:type :begin-test-var, :var test-var})
  (binding [ctest/*report-counters* (ref ctest/*initial-report-counters*)
            test/*report* (partial report {:print-color true})]
    (let [result (->> (test-var)
                      (test/run-test! system))]
      (ctest/do-report {:type :end-test-var, :var test-var})
      (merge testable (test-results->kaocha @ctest/*report-counters*)))))

(s/def :kaocha.greenlight/test-var var?)
(s/def :kaocha.type/greenlight.var (s/keys :req [::testable/type :kaocha.greenlight/test-var]))

(hierarchy/derive! :kaocha.type/greenlight.var :kaocha.testable.type/leaf)

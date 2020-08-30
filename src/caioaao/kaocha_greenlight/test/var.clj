(ns caioaao.kaocha-greenlight.test.var
  (:require [kaocha.testable :as testable]
            [clojure.test :as ctest]
            [clojure.spec.alpha :as s]
            [kaocha.hierarchy :as hierarchy]
            [greenlight.test :as test]
            [greenlight.step :as step]))

(defn test-results->kaocha [rs]
  {:kaocha.result/pass    (:pass rs 0)
   :kaocha.result/error   (:error rs 0)
   :kaocha.result/fail    (:fail rs 0)
   :kaocha.result/pending (:pending rs 0)
   :kaocha.result/count   1})

(defn report [event]
  (let [{:keys [step type]}                     event
        {::step/keys [outcome reports message]} step]
    (when (= type :step-end)
      (run! ctest/do-report reports))
    (when (= outcome :error)
      (ctest/report {:type    :error
                     :message "Uncaught exception, not in assertion."
                     :actual  message}))))

(defmethod testable/-run :caioaao.kaocha-greenlight.test/var
  [{:caioaao.kaocha-greenlight.test/keys [test-var] :as testable}
   {:caioaao.kaocha-greenlight.test/keys [system]}]
  (ctest/do-report {:type :begin-test-var, :var test-var})
  (binding [ctest/*report-counters* (ref ctest/*initial-report-counters*)
            test/*report* report]
    (test/run-test! system (test-var))
    (ctest/do-report {:type :end-test-var, :var test-var})
    (merge testable (test-results->kaocha @ctest/*report-counters*))))

(s/def :caioaao.kaocha-greenlight.test/test-var var?)
(s/def :caioaao.kaocha-greenlight.test/var (s/keys :req [::testable/type :caioaao.kaocha-greenlight.test/test-var]))

(hierarchy/derive! :caioaao.kaocha-greenlight.test/var :kaocha.testable.type/leaf)

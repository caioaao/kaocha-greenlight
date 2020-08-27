(ns caioaao.kaocha-greenlight.runner
  (:require [com.stuartsierra.component :as component]
            [kaocha.testable :as testable]))

(defn- resolve-system [system-fn-symbol]
  (let [ns-name (symbol (namespace system-fn-symbol))]
    (when-not (find-ns ns-name)
      (require ns-name))
    ((resolve system-fn-symbol))))

(defn- run-testables
  [testable test-plan]
  (let [tests   (:kaocha.test-plan/tests testable)
        results (testable/run-testables tests test-plan)]
    (-> testable
        (dissoc :kaocha.test-plan/tests)
        (assoc :kaocha.result/tests results))))

(defn run
  [testable
   {:caioaao.kaocha-greenlight/keys [new-system system-scope]
    :or                             {system-scope :test}
    :as                             test-plan}
   level]
  (if-not (= system-scope level)
    (run-testables testable test-plan)
    (let [system    (-> new-system
                        resolve-system
                        component/start)
          test-plan (assoc test-plan
                           :caioaao.kaocha-greenlight.test/system
                           system)]
      (try
        (run-testables testable test-plan)
        (finally
          (component/stop system))))))

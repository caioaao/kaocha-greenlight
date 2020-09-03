(ns caioaao.kaocha-greenlight.runner
  (:require
   [com.stuartsierra.component :as component]))

(defn run
  [testable test-plan level run-fn]
  (let [config       (select-keys testable
                                  [:caioaao.kaocha-greenlight.test/system
                                   :caioaao.kaocha-greenlight/system-scope])
        test-plan    (merge test-plan config)
        system-scope (get test-plan
                          :caioaao.kaocha-greenlight/system-scope
                          :test)]
    (if-not (= system-scope level)
      (run-fn testable test-plan)
      (let [system    (-> test-plan
                          :caioaao.kaocha-greenlight.test/system
                          component/start)
            test-plan (assoc test-plan
                             :caioaao.kaocha-greenlight.test/system
                             system)]
        (try
          (run-fn testable test-plan)
          (finally
            (component/stop system)))))))

(ns caioaao.kaocha-greenlight.test
  (:require [clojure.spec.alpha :as s]
            [clojure.test :as t]
            [com.stuartsierra.component :as component]
            [kaocha.hierarchy :as hierarchy]
            [kaocha.load :as load]
            [kaocha.testable :as testable]
            [kaocha.type.ns :as type.ns]
            [caioaao.kaocha-greenlight.report]))

(defn- ns->testable [ns]
  (assoc (type.ns/->testable ns) :kaocha.testable/type ::ns))

(defn get-system [system-fn-symbol]
  (let [ns-name (symbol (namespace system-fn-symbol))]
    (when-not (find-ns ns-name)
      (require ns-name))
    ((resolve system-fn-symbol))))

(defmethod testable/-load :caioaao.kaocha-greenlight/test
  [{:caioaao.kaocha-greenlight/keys [new-system] :as testable}]
  (assoc (load/load-test-namespaces testable ns->testable)
         ::testable/desc (str (name (::testable/id testable)) " (greenlight)")
         :caioaao.kaocha-greenlight/system (get-system new-system)))

;; TODO add configuration parameter to restart system on each name space instead of here
(defmethod testable/-run :caioaao.kaocha-greenlight/test
  [{:caioaao.kaocha-greenlight/keys [system] :as testable} test-plan]
  (t/do-report {:type :begin-test-suite})
  (let [system  (component/start system)
        results (try
                  (testable/run-testables (:kaocha.test-plan/tests testable)
                                          (assoc test-plan ::system system))
                  (finally (component/stop system)))
        testable (-> testable
                     (dissoc :kaocha.test-plan/tests)
                     (assoc :kaocha.result/tests results))]
    (t/do-report {:type :end-test-suite})
    testable))

(s/def :caioaao.kaocha-greenlight/new-system symbol?)

(s/def :caioaao.kaocha-greenlight/test (s/keys :req [::testable/type ::testable/id
                                                     :kaocha/ns-patterns :kaocha/source-paths :kaocha/test-paths
                                                     :caioaao.kaocha-greenlight/new-system]))

(hierarchy/derive! :caioaao.kaocha-greenlight/test :kaocha.testable.type/suite)

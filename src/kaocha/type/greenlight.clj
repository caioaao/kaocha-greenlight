(ns kaocha.type.greenlight
  (:require [clojure.spec.alpha :as s]
            [clojure.test :as t]
            [com.stuartsierra.component :as component]
            [kaocha.hierarchy :as hierarchy]
            [kaocha.load :as load]
            [kaocha.testable :as testable]
            [kaocha.type.ns :as type.ns]
            [kaocha.type.greenlight.report]))

(defn- ns->testable [ns]
  (assoc (type.ns/->testable ns) :kaocha.testable/type :kaocha.type/greenlight.ns))

(defn get-system [system-fn-symbol]
  (let [ns-name (symbol (namespace system-fn-symbol))]
    (when-not (find-ns ns-name)
      (require ns-name))
    ((resolve system-fn-symbol))))

(defmethod testable/-load :kaocha.type/greenlight
  [{:kaocha.greenlight/keys [new-system] :as testable}]
  (assoc (load/load-test-namespaces testable ns->testable)
         ::testable/desc (str (name (::testable/id testable)) " (greenlight)")
         :kaocha.greenlight/system (get-system new-system)))

;; TODO add configuration parameter to restart system on each name space instead of here
(defmethod testable/-run :kaocha.type/greenlight
  [{:kaocha.greenlight/keys [system] :as testable} test-plan]
  (t/do-report {:type :begin-test-suite})
  (let [system  (component/start system)
        results (try
                  (testable/run-testables (:kaocha.test-plan/tests testable)
                                          (assoc test-plan :kaocha.greenlight/system system))
                  (finally (component/stop system)))
        testable (-> testable
                     (dissoc :kaocha.test-plan/tests)
                     (assoc :kaocha.result/tests results))]
    (t/do-report {:type :end-test-suite})
    testable))

(s/def :kaocha.greenlight/new-system symbol?)

(s/def :kaocha.type/greenlight (s/keys :req [::testable/type ::testable/id
                                             :kaocha/ns-patterns :kaocha/source-paths :kaocha/test-paths
                                             :kaocha.greenlight/new-system]))

(hierarchy/derive! :kaocha.type/greenlight :kaocha.testable.type/suite)

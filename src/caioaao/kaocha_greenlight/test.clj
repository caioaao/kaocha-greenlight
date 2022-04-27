(ns caioaao.kaocha-greenlight.test
  (:require
   [caioaao.kaocha-greenlight.runner :as runner]
   [caioaao.kaocha-greenlight.test.ns]
   [clojure.spec.alpha :as s]
   [kaocha.hierarchy :as hierarchy]
   [kaocha.load :as load]
   [kaocha.test-suite :as test-suite]
   [kaocha.testable :as testable]
   [kaocha.type.ns :as type.ns]))

(defn ^:private resolve-system
  [system-fn-symbol]
  (let [ns-name (symbol (namespace system-fn-symbol))]
    (when-not (find-ns ns-name)
      (require ns-name))
    ((resolve system-fn-symbol))))

(defn ^:private ns->testable
  [ns]
  (assoc (type.ns/->testable ns) :kaocha.testable/type ::ns))

(defmethod testable/-load :caioaao.kaocha-greenlight/test
  [testable]
  (-> (load/load-test-namespaces testable ns->testable)
      (testable/add-desc "greenlight")
      (assoc :caioaao.kaocha-greenlight.test/system
             (-> testable
                 :caioaao.kaocha-greenlight/new-system
                 resolve-system))))

(defmethod testable/-run :caioaao.kaocha-greenlight/test
  [testable test-plan]
  (runner/run testable test-plan :test test-suite/run))

(s/def :caioaao.kaocha-greenlight/new-system symbol?)

(s/def :caioaao.kaocha-greenlight/system-scope #{:test :ns :var})

(s/def :caioaao.kaocha-greenlight/test
  (s/keys :req [::testable/type
                ::testable/id
                :kaocha/ns-patterns
                :kaocha/source-paths
                :kaocha/test-paths
                :caioaao.kaocha-greenlight/new-system]
          :opt [:caioaao.kaocha-greenlight/system-scope]))

(hierarchy/derive! :caioaao.kaocha-greenlight/test :kaocha.testable.type/suite)

(ns caioaao.kaocha-greenlight.test
  (:require [clojure.spec.alpha :as s]
            [clojure.test :as t]
            [kaocha.hierarchy :as hierarchy]
            [kaocha.load :as load]
            [kaocha.testable :as testable]
            [kaocha.type.ns :as type.ns]
            [caioaao.kaocha-greenlight.report]
            [caioaao.kaocha-greenlight.runner :as runner]))

(defn- ns->testable [ns]
  (assoc (type.ns/->testable ns) :kaocha.testable/type ::ns))

(defmethod testable/-load :caioaao.kaocha-greenlight/test
  [testable]
  (assoc (load/load-test-namespaces testable ns->testable)
         ::testable/desc (str (name (::testable/id testable)) " (greenlight)")))

(defmethod testable/-run :caioaao.kaocha-greenlight/test
  [testable test-plan]
  (t/do-report {:type :begin-test-suite})
  (let [testable (runner/run testable test-plan :test)]
    (t/do-report {:type :end-test-suite})
    testable))

(s/def :caioaao.kaocha-greenlight/new-system symbol?)

(s/def :caioaao.kaocha-greenlight/system-scope #{:test :ns})

(s/def :caioaao.kaocha-greenlight/test
  (s/keys :req [::testable/type
                ::testable/id
                :kaocha/ns-patterns
                :kaocha/source-paths
                :kaocha/test-paths
                :caioaao.kaocha-greenlight/new-system]
          :opt [:caioaao.kaocha-greenlight/system-scope]))

(hierarchy/derive! :caioaao.kaocha-greenlight/test :kaocha.testable.type/suite)

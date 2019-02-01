(ns caioaao.kaocha-greenlight.ns
  (:require [kaocha.testable :as testable]
            [clojure.test :as t]
            [clojure.spec.alpha :as s]
            [kaocha.output :as output]
            [kaocha.hierarchy :as hierarchy]))

(defn- test-var->testable [v]
  {::testable/type             :kaocha.type/greenlight.var
   ::testable/id               (keyword (str v))
   :caioaao.kaocha-greenlight/test-var v})

(defn- test-vars [ns]
  (->> ns ns-interns vals (filter (comp :greenlight.test/test meta))))

(defmethod testable/-load :kaocha.type/greenlight.ns
  [testable]
  (let [ns-name (:kaocha.ns/name testable)]
    (try
      (when-not (find-ns ns-name)
        (require ns-name))
      (->> (test-vars (:kaocha.ns/name testable))
           (map test-var->testable)
           (assoc testable :kaocha.test-plan/tests))
      (catch Throwable t
        (output/warn "Failed loading " ns-name ": " (.getMessage t))
        #_(kaocha.stacktrace/print-cause-trace t)
        (assoc testable :kaocha.test-plan/load-error t)))))

(defmethod testable/-run :kaocha.type/greenlight.ns
  [testable test-plan]
  (t/do-report {:type :begin-test-ns, :ns (:kaocha.ns/name testable)})
  (let [results  (testable/run-testables (:kaocha.test-plan/tests testable) test-plan)
        testable (-> testable
                     (dissoc :kaocha.test-plan/tests)
                     (assoc :kaocha.result/tests results))]
    (t/do-report {:type :end-test-ns, :ns (:kaocha.ns/ns testable)})
    testable))

(s/def :kaocha.type/greenlight.ns (s/keys :req [::testable/type ::testable/id :kaocha.ns/name]))
(hierarchy/derive! :kaocha.type/greenlight.ns :kaocha.testable.type/group)

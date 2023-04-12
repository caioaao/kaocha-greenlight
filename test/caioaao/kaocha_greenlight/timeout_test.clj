(ns caioaao.kaocha-greenlight.timeout-test
  (:require
   [clojure.test :refer [deftest is testing]]
   [kaocha.api :as api]
   [kaocha.result :as result]
   [matcher-combinators.matchers :as matchers]
   [matcher-combinators.parser :refer [mimic-matcher]]))

(mimic-matcher matchers/equals clojure.lang.Var)

(defn new-system
  [& _]
  {:greenlight.test-test/component {}})

(def timeout-config
  {:kaocha/tests [{:kaocha.testable/type :caioaao.kaocha-greenlight/test
                   :kaocha.testable/id   :integration-test
                   :kaocha/ns-patterns   ["timeout-suite.*-test$"]
                   :kaocha/source-paths  ["src"]
                   :kaocha/test-paths    ["test"]

                   :caioaao.kaocha-greenlight/new-system 'caioaao.kaocha-greenlight.timeout-test/new-system}]})

(deftest timeout-test
  (testing "correctly handles timeouts"
    (is (match? #:kaocha.result{:count   1
                                :error   1
                                :fail    0
                                :pass    0
                                :pending 0}
                (-> timeout-config
                    api/run
                    result/testable-totals)))))

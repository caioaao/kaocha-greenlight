(ns caioaao.kaocha-greenlight.error-test
  (:require [clojure.test :refer [deftest testing is]]
            [com.stuartsierra.component :as component]
            [kaocha.api :as api]
            [kaocha.result :as result]
            [matcher-combinators.test]
            [matcher-combinators.matchers :as matchers]
            [matcher-combinators.parser :refer [mimic-matcher]]))

(mimic-matcher matchers/equals clojure.lang.Var)

(defn new-system
  [& _]
  (component/system-map :greenlight.test-test/component {}))

(def error-config
  {:kaocha/tests [{:kaocha.testable/type :caioaao.kaocha-greenlight/test
                   :kaocha.testable/id   :integration-test
                   :kaocha/ns-patterns   ["error-suite.*-test$"]
                   :kaocha/source-paths  ["src"]
                   :kaocha/test-paths    ["test"]

                   :caioaao.kaocha-greenlight/new-system 'caioaao.kaocha-greenlight.error-test/new-system}]})

(deftest error-tests
  (testing "correctly handles errors"
    (is (match? #:kaocha.result{:count   1
                                :error   1
                                :fail    0
                                :pass    0
                                :pending 0}
                (-> error-config
                    api/run
                    result/testable-totals)))))

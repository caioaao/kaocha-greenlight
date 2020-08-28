(ns caioaao.kaocha-greenlight.scope-test
  (:require [clojure.test :refer [deftest testing is]]
            [com.stuartsierra.component :as component]
            [kaocha.testable :as testable]
            [kaocha.result]
            [matcher-combinators.test]
            [matcher-combinators.parser :refer [mimic-matcher]]
            [caioaao.kaocha-greenlight.test-suite.blue-test]
            [caioaao.kaocha-greenlight.test-suite.red-test]
            [matcher-combinators.matchers :as matchers]))

(mimic-matcher matchers/equals clojure.lang.Var)

(def starts (atom 0))
(def stops (atom 0))

(defn new-system [& _]
  (component/system-map :greenlight.test-test/component
                        (with-meta {}
                          {`component/start (fn [this]
                                              (swap! starts inc)
                                              this)
                           `component/stop  (fn [this]
                                              (swap! stops inc)
                                              this)})))

(def test-suite-test {::testable/type                       :caioaao.kaocha-greenlight/test
                      ::testable/id                         :integration-test
                      :caioaao.kaocha-greenlight/new-system 'caioaao.kaocha-greenlight.scope-test/new-system
                      :kaocha/ns-patterns                   ["scope-suite.*-test$"]
                      :kaocha/source-paths                  ["src"]
                      :kaocha/test-paths                    ["test"]})

(def test-suite-ns {::testable/type                         :caioaao.kaocha-greenlight/test
                    ::testable/id                           :integration-test
                    :caioaao.kaocha-greenlight/new-system   'caioaao.kaocha-greenlight.scope-test/new-system
                    :caioaao.kaocha-greenlight/system-scope :ns
                    :kaocha/ns-patterns                     ["scope-suite.*-test$"]
                    :kaocha/source-paths                    ["src"]
                    :kaocha/test-paths                      ["test"]})

(deftest scope-tests
  (testing "system is created once when system-scope is :test"
    (let [test-test-plan (testable/load test-suite-test)]
      (reset! starts 0)
      (reset! stops 0)
      (testable/run test-test-plan test-test-plan)
      (is (= @starts 1))
      (is (= @stops 1))))

  (testing "system is created per ns when system-scope is :ns"
    (let [ns-test-plan (testable/load test-suite-ns)]
      (reset! starts 0)
      (reset! stops 0)
      (testable/run ns-test-plan ns-test-plan)
      (is (= @starts 3))
      (is (= @stops 3)))))

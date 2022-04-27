(ns caioaao.kaocha-greenlight.scope-test
  (:require
   [clojure.test :refer [deftest testing is]]
   [com.stuartsierra.component :as component]
   [kaocha.api :as api]
   [matcher-combinators.matchers :as matchers]
   [matcher-combinators.parser :refer [mimic-matcher]]))

(mimic-matcher matchers/equals clojure.lang.Var)

(def starts (atom 0))
(def stops (atom 0))

(defn new-system
  [& _]
  (component/system-map :greenlight.test-test/component
                        (with-meta {}
                          {`component/start (fn [this]
                                              (swap! starts inc)
                                              this)
                           `component/stop  (fn [this]
                                              (swap! stops inc)
                                              this)})))

(def test-config
  {:kaocha/tests [{:kaocha.testable/type :caioaao.kaocha-greenlight/test
                   :kaocha.testable/id   :integration-test
                   :kaocha/ns-patterns   ["scope-suite.*-test$"]
                   :kaocha/source-paths  ["src"]
                   :kaocha/test-paths    ["test"]

                   :caioaao.kaocha-greenlight/new-system 'caioaao.kaocha-greenlight.scope-test/new-system}]})

(def ns-config
  {:kaocha/tests [{:kaocha.testable/type :caioaao.kaocha-greenlight/test
                   :kaocha.testable/id   :integration-test
                   :kaocha/ns-patterns   ["scope-suite.*-test$"]
                   :kaocha/source-paths  ["src"]
                   :kaocha/test-paths    ["test"]

                   :caioaao.kaocha-greenlight/new-system   'caioaao.kaocha-greenlight.scope-test/new-system
                   :caioaao.kaocha-greenlight/system-scope :ns}]})

(def var-config
  {:kaocha/tests [{:kaocha.testable/type :caioaao.kaocha-greenlight/test
                   :kaocha.testable/id   :integration-test
                   :kaocha/ns-patterns   ["scope-suite.*-test$"]
                   :kaocha/source-paths  ["src"]
                   :kaocha/test-paths    ["test"]

                   :caioaao.kaocha-greenlight/new-system   'caioaao.kaocha-greenlight.scope-test/new-system
                   :caioaao.kaocha-greenlight/system-scope :var}]})

(deftest scope-tests
  (testing "system is created once when system-scope is :test"
    (reset! starts 0)
    (reset! stops 0)
    (api/run test-config)
    (is (= @starts 1))
    (is (= @stops 1)))

  (testing "system is created per ns when system-scope is :ns"
    (reset! starts 0)
    (reset! stops 0)
    (api/run ns-config)
    (is (= @starts 3))
    (is (= @stops 3)))

  (testing "system is created per var when system-scope is :var"
    (reset! starts 0)
    (reset! stops 0)
    (api/run var-config)
    (is (= @starts 6))
    (is (= @stops 6))))

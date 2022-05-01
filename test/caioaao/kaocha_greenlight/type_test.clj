(ns caioaao.kaocha-greenlight.type-test
  (:require
   [caioaao.kaocha-greenlight.test-suite.blue-test]
   [caioaao.kaocha-greenlight.test-suite.red-test]
   [clojure.test :refer [deftest is testing]]
   [com.stuartsierra.component :as component]
   [greenlight.runner :as runner]
   [kaocha.api :as api]
   [kaocha.result :as result]
   [kaocha.testable :as testable]
   [matcher-combinators.matchers :as matchers]
   [matcher-combinators.parser :refer [mimic-matcher]]
   [matcher-combinators.test]))

(mimic-matcher matchers/equals clojure.lang.Var)

(defn new-system-1
  [& _]
  (component/system-map :greenlight.test-test/component 6))

(defn new-system-2
  [& _]
  (component/system-map :greenlight.test-test/component 5))

(def test-suite-blue
  {::testable/type                       :caioaao.kaocha-greenlight/test
   ::testable/id                         :integration-blue
   :caioaao.kaocha-greenlight/new-system 'caioaao.kaocha-greenlight.type-test/new-system-1
   :kaocha/ns-patterns                   ["test-suite.blue-test$"]
   :kaocha/source-paths                  ["src"]
   :kaocha/test-paths                    ["test"]})

(def test-suite-red
  {::testable/type                       :caioaao.kaocha-greenlight/test
   ::testable/id                         :integration-red
   :caioaao.kaocha-greenlight/new-system 'caioaao.kaocha-greenlight.type-test/new-system-2
   :kaocha/ns-patterns                   ["test-suite.red-test$"]
   :kaocha/source-paths                  ["src"]
   :kaocha/test-paths                    ["test"]})

(def full-test-suite
  {::testable/type                       :caioaao.kaocha-greenlight/test
   ::testable/id                         :integration-full
   :caioaao.kaocha-greenlight/new-system 'caioaao.kaocha-greenlight.type-test/new-system-1
   :kaocha/ns-patterns                   ["test-suite.*-test$"]
   :kaocha/source-paths                  ["src"]
   :kaocha/test-paths                    ["test"]})

(deftest loading-tests
  (testing "contains test suite info"
    (is (match? test-suite-blue (testable/load test-suite-blue)))
    (is (match? test-suite-red (testable/load test-suite-red))))

  (testing "filters the correct namespaces"
    (is (match? {:kaocha.test-plan/tests [{:kaocha.testable/type   :caioaao.kaocha-greenlight.test/ns
                                           :kaocha.testable/id     :caioaao.kaocha-greenlight.test-suite.blue-test
                                           :kaocha.testable/desc   "caioaao.kaocha-greenlight.test-suite.blue-test"
                                           :kaocha.ns/name         'caioaao.kaocha-greenlight.test-suite.blue-test
                                           :kaocha.test-plan/tests [{:kaocha.testable/type :caioaao.kaocha-greenlight.test/var
                                                                     :kaocha.testable/id   :caioaao.kaocha-greenlight.test-suite.blue-test/sample-test
                                                                     :kaocha.var/var       #'caioaao.kaocha-greenlight.test-suite.blue-test/sample-test}]}]}
                (testable/load test-suite-blue)))
    (is (match? {:kaocha.test-plan/tests [{:kaocha.testable/type   :caioaao.kaocha-greenlight.test/ns
                                           :kaocha.testable/id     :caioaao.kaocha-greenlight.test-suite.red-test
                                           :kaocha.testable/desc   "caioaao.kaocha-greenlight.test-suite.red-test"
                                           :kaocha.ns/name         'caioaao.kaocha-greenlight.test-suite.red-test
                                           :kaocha.test-plan/tests [{:kaocha.testable/type :caioaao.kaocha-greenlight.test/var
                                                                     :kaocha.testable/id   :caioaao.kaocha-greenlight.test-suite.red-test/sample-test
                                                                     :kaocha.var/var       #'caioaao.kaocha-greenlight.test-suite.red-test/sample-test}]}]}
                (testable/load test-suite-red))))

  (testing "description is correct"
    (is (= "integration-blue (greenlight)"
           (:kaocha.testable/desc (testable/load test-suite-blue))))
    (is (= "integration-red (greenlight)"
           (:kaocha.testable/desc (testable/load test-suite-red))))))

(deftest running-tests
  (testing "counts are correctly tracked"
    (is (match? #:kaocha.result{:count   1
                                :error   0
                                :fail    0
                                :pass    13
                                :pending 0}
                (-> {:kaocha/tests [test-suite-blue]}
                    api/run
                    result/testable-totals)))

    (is (match? #:kaocha.result{:count   1
                                :error   0
                                :fail    1
                                :pass    9
                                :pending 0}
                (-> {:kaocha/tests [test-suite-red]}
                    api/run
                    result/testable-totals)))

    (is (match? #:kaocha.result{:count   2
                                :error   0
                                :fail    0
                                :pass    26
                                :pending 0}
                (-> {:kaocha/tests [full-test-suite]}
                    api/run
                    result/testable-totals)))))

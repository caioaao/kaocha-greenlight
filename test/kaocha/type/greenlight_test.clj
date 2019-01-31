(ns kaocha.type.greenlight-test
  (:require [clojure.test :refer :all]
            [com.stuartsierra.component :as component]
            [kaocha.testable :as testable]
            [kaocha.result]
            [matcher-combinators.test]
            [matcher-combinators.parser :refer [mimic-matcher]]
            [kaocha.greenlight.blue-test]
            [kaocha.greenlight.red-test]
            [matcher-combinators.matchers :as matchers]))

(mimic-matcher matchers/equals clojure.lang.Var)

(defn new-system-1 [& _]
  (component/system-map :greenlight.test-test/component 6))

(defn new-system-2 [& _]
  (component/system-map :greenlight.test-test/component 5))

(def test-suite-blue {::testable/type               :kaocha.type/greenlight
                      ::testable/id                 :integration-blue
                      :kaocha.greenlight/new-system #'kaocha.type.greenlight-test/new-system-1
                      :kaocha/ns-patterns           ["blue-test$"]
                      :kaocha/source-paths          ["src"]
                      :kaocha/test-paths            ["test"]})

(def test-suite-red {::testable/type               :kaocha.type/greenlight
                     ::testable/id                 :integration-red
                     :kaocha.greenlight/new-system #'kaocha.type.greenlight-test/new-system-2
                     :kaocha/ns-patterns           ["red-test$"]
                     :kaocha/source-paths          ["src"]
                     :kaocha/test-paths            ["test"]})

(def full-test-suite {::testable/type               :kaocha.type/greenlight
                      ::testable/id                 :integration-blue
                      :kaocha.greenlight/new-system #'kaocha.type.greenlight-test/new-system-1
                      :kaocha/ns-patterns           ["-test$"]
                      :kaocha/source-paths          ["src"]
                      :kaocha/test-paths            ["test"]})

(deftest loading-tests
  (testing "contains test suite info"
    (is (match? test-suite-blue (testable/load test-suite-blue)))
    (is (match? test-suite-red (testable/load test-suite-red))))

  (testing "filters the correct namespaces"
    (is (match? {:kaocha.test-plan/tests [{:kaocha.testable/type   :kaocha.type/greenlight.ns
                                           :kaocha.testable/id     :kaocha.greenlight.blue-test
                                           :kaocha.testable/desc   "kaocha.greenlight.blue-test"
                                           :kaocha.ns/name         'kaocha.greenlight.blue-test
                                           :kaocha.test-plan/tests [{:kaocha.testable/type       :kaocha.type/greenlight.var
                                                                     :kaocha.testable/id         :#'kaocha.greenlight.blue-test/sample-test
                                                                     :kaocha.greenlight/test-var #'kaocha.greenlight.blue-test/sample-test}]}]}
                (testable/load test-suite-blue)))
    (is (match? {:kaocha.test-plan/tests [{:kaocha.testable/type   :kaocha.type/greenlight.ns
                                           :kaocha.testable/id     :kaocha.greenlight.red-test
                                           :kaocha.testable/desc   "kaocha.greenlight.red-test"
                                           :kaocha.ns/name         'kaocha.greenlight.red-test
                                           :kaocha.test-plan/tests [{:kaocha.testable/type       :kaocha.type/greenlight.var
                                                                     :kaocha.testable/id         :#'kaocha.greenlight.red-test/sample-test
                                                                     :kaocha.greenlight/test-var #'kaocha.greenlight.red-test/sample-test}]}]}
                (testable/load test-suite-red))))

  (testing "description is correct"
    (is (= "integration-blue (greenlight)"
           (:kaocha.testable/desc (testable/load test-suite-blue))))
    (is (= "integration-red (greenlight)"
           (:kaocha.testable/desc (testable/load test-suite-red))))))

(let [blue-test-plan    (testable/load test-suite-blue)
      red-test-plan     (testable/load test-suite-red)
      full-test-plan    (testable/load full-test-suite)
      blue-results      (testable/run blue-test-plan blue-test-plan)
      red-results       (testable/run red-test-plan red-test-plan)
      full-test-results (testable/run full-test-plan full-test-plan)]
  (deftest running-tests
    (testing "counts are correctly tracked"
      (is (match? #:kaocha.result{:count   13
                                  :error   0
                                  :fail    0
                                  :pass    13
                                  :pending 0}
                  (kaocha.result/testable-totals blue-results)))

      (is (match? #:kaocha.result{:count   10
                                  :error   0
                                  :fail    1
                                  :pass    9
                                  :pending 0}
                  (kaocha.result/testable-totals red-results)))

      (is (match? #:kaocha.result{:count   26
                                  :error   0
                                  :fail    0
                                  :pass    26
                                  :pending 0}
                  (kaocha.result/testable-totals full-test-results))))))

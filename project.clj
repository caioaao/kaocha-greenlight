(defproject caioaao/kaocha-greenlight "0.6.3"
  :description "Kaocha extension to run amperity/greenlight tests"
  :url "http://github.com/caioaao/kaocha-greenlight"
  :license {:name "EPL-2.0 OR GPL-2.0-or-later WITH Classpath-exception-2.0"
            :url  "https://www.eclipse.org/legal/epl-2.0/"}
  :dependencies [[org.clojure/clojure "1.11.1"]
                 [amperity/greenlight "0.6.1"]
                 [lambdaisland/kaocha "1.66.1034"]]
  :aliases {"kaocha" ["run" "-m" "kaocha.runner"]}
  :profiles {:dev {:dependencies [[nubank/matcher-combinators "3.5.0"]
                                  [orchestra "2021.01.01-1"]
                                  [com.stuartsierra/component "1.1.0"]
                                  [org.clojure/test.check "1.1.0"]
                                  [expound "0.9.0"]]
                   :source-paths ["dev"]
                   :plugins      [[lein-cljfmt "0.8.0"]]}}
  :repl-options {:init-ns kaocha-greenlight.core}
  :release-tasks [["deploy" "clojars"]]
  :deploy-repositories [["clojars" {:url           "https://clojars.org/repo"
                                    :sign-releases false
                                    :username      :env/clojars_username
                                    :password      :env/clojars_password}]])

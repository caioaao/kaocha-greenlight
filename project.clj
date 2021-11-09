(defproject caioaao/kaocha-greenlight "0.5.1-SNAPSHOT"
  :description "Kaocha extension to run amperity/greenlight tests"
  :url "http://github.com/caioaao/kaocha-greenlight"
  :license {:name "EPL-2.0 OR GPL-2.0-or-later WITH Classpath-exception-2.0"
            :url  "https://www.eclipse.org/legal/epl-2.0/"}
  :dependencies [[org.clojure/clojure "1.10.1"]
                 [amperity/greenlight "0.5.0"]
                 [lambdaisland/kaocha "1.0.672"]]
  :aliases {"test" ["run" "-m" "kaocha.runner"]}
  :profiles {:dev {:dependencies [[nubank/matcher-combinators "0.4.2"]
                                  [orchestra "2020.07.12-1"]
                                  [org.clojure/test.check "1.1.0"]
                                  [expound "0.8.5"]]
                   :source-paths ["dev"]
                   :plugins      [[lein-cljfmt "0.6.1"]]}}
  :repl-options {:init-ns kaocha-greenlight.core}
  :release-tasks [["deploy" "clojars"]]
  :deploy-repositories [["clojars" {:url           "https://clojars.org/repo"
                                    :sign-releases false
                                    :username      :env/clojars_username
                                    :password      :env/clojars_password}]])

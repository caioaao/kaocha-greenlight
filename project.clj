(defproject caioaao/kaocha-greenlight "0.1.2"
  :description "Kaocha extension to run amperity/greenlight tests"
  :url "http://github.com/caioaao/kaocha-greenlight"
  :license {:name "EPL-2.0 OR GPL-2.0-or-later WITH Classpath-exception-2.0"
            :url  "https://www.eclipse.org/legal/epl-2.0/"}
  :dependencies [[org.clojure/clojure "1.9.0"]
                 [amperity/greenlight "0.1.2"]
                 [lambdaisland/kaocha "0.0-389"]]
  :profiles {:dev {:dependencies [[nubank/matcher-combinators "0.4.2"]]
                   :plugins      [[lein-cljfmt "0.6.1"]]}}
  :repl-options {:init-ns kaocha-greenlight.core}
  :release-tasks [["deploy" "clojars"]
                  ["change" "version"
                   "leiningen.release/bump-version" "patch"]
                  ["vcs" "commit"]
                  ["vcs" "push" "origin" "HEAD:master"]]
  :deploy-repositories [["clojars" {:url           "https://clojars.org/repo"
                                    :sign-releases false
                                    :username      :env/clojars_username
                                    :password      :env/clojars_password}]])

[![CircleCI](https://circleci.com/gh/caioaao/kaocha-greenlight/tree/master.svg?style=svg)](https://circleci.com/gh/caioaao/kaocha-greenlight/tree/master) [![Clojars Project](https://img.shields.io/clojars/v/caioaao/kaocha-greenlight.svg)](https://clojars.org/caioaao/kaocha-greenlight)

# kaocha-greenlight

Kaocha plugin to run [greenlight](/amperity/greenlight) tests.

## Installing

The project is published through Clojars with the identifier `caioaao/kaocha-greenlight`. You can find version information for the latest release at https://clojars.org/caioaao/kaocha-greenlight.

## Usage

Declare a test suite on your [Kaocha config file](https://cljdoc.org/d/lambdaisland/kaocha/0.0-413/doc/3-configuration) with the type `:caioaao.kaocha-greenlight/test`. You'll also need to provide a value for `:caioaao.kaocha-greenlight/new-system`, which should be a function that receives no arguments and returns [a stuartsierra's system map](https://github.com/stuartsierra/component).

A `tests.edn` example:

```clojure
#kaocha/v1
{:tests [{:id           :integration
          :type         :caioaao.kaocha-greenlight/test
          :test-paths   ["test"]
          :source-paths ["src"]
          :ns-patterns  ["-flow$"]
          :caioaao.kaocha-greenlight/new-system my.app/system-map}]}
```

You may configure `kaocha-greenlight` to create a new system for each namespace,
which can be useful if you'd like a clean state for each namespace, e.g. if
you're using ephemeral datastores:

```clojure
#kaocha/v1
{:tests [{:id           :integration
          :type         :caioaao.kaocha-greenlight/test
          :test-paths   ["test"]
          :source-paths ["src"]
          :ns-patterns  ["-flow$"]
          :caioaao.kaocha-greenlight/new-system my.app/system-map
          :caioaao.kaocha-greenlight/system-scope :ns}]}
```

For documentation on how to run tests, refer to [Kaocha](/lambdaisland/kaocha). For documentation regarding writing tests, refer to [Greenlight](/amperity/greenlight).

## License

Copyright © 2019 Caio Oliveira

This program and the accompanying materials are made available under the
terms of the Eclipse Public License 2.0 which is available at
http://www.eclipse.org/legal/epl-2.0.

This Source Code may also be made available under the following Secondary
Licenses when the conditions for such availability set forth in the Eclipse
Public License, v. 2.0 are satisfied: GNU General Public License as published by
the Free Software Foundation, either version 2 of the License, or (at your
option) any later version, with the GNU Classpath Exception which is available
at https://www.gnu.org/software/classpath/license.html.

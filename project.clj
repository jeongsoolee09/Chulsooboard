(defproject chulsooboard "0.1.0-SNAPSHOT"
  :description ""
  :url "https://github.com/jeongsoolee09/chulsooboard"
  :license {:name "GPL-2.0-or-later WITH Classpath-exception-2.0"
            :url "https://www.eclipse.org/legal/epl-2.0/"}
  :dependencies [[org.clojure/clojure "1.10.1"]
                 [org.clojure/tools.namespace "1.1.0"]
                 [enlive "1.1.6"]
                 [progrock "0.1.2"]
                 [clojure.java-time "0.3.2"]
                 [org.clojure/tools.trace "0.7.10"]
                 [seancorfield/next.jdbc "1.1.613"]]
  :resource-paths ["resources/postgresql-42.2.18.jar"]
  :main ^:skip-aot chulsooboard.core
  :target-path "target/%s"
  :profiles {:uberjar {:aot :all
                       :jvm-opts ["-Dclojure.compiler.direct-linking=true"]}})

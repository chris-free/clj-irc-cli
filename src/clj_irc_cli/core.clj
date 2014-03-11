;; http://oreilly.com/pub/h/1963
;; http://nakkaya.com/2010/02/10/a-simple-clojure-irc-client/
;; http://www.haskell.org/haskellwiki/Roll_your_own_IRC_bot#Where_to_now.3F

(ns clj-irc-cli.core
  (:require [clojure.core.async :as a]))

(use 'lamina.core 'aleph.tcp 'gloss.core)

#_(do
    (def ch
      (wait-for-result
       (tcp-client {:host "irc.freenode.net",
                    :port 6667,
                    :frame (string :utf-8 :delimiters ["\r\n"])})))

    
    (a/go-loop []
      (when-let [msg (wait-for-message ch)]
        (when (.contains msg "PING")
          (enqueue ch "PONG"))
        (println msg)
        (recur)))

    (enqueue ch "NICK k3ny")
    (enqueue ch "USER k3ny 8 *  : Paul Mutton")
    (enqueue ch "JOIN #clojure"))


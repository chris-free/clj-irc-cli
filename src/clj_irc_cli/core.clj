(ns clj-irc-cli.core)

(use 'lamina.core 'aleph.tcp 'gloss.core)

#_(do
    (def ch
      (wait-for-result
       (tcp-client {:host "localhost",
                    :port 80,
                    :frame (string :utf-8 :delimiters ["\r\n"])})))

    (enqueue ch "GET / HTTP/1.1 \n HOST: localhost \n")

    (wait-for-message ch))


(comment "tcp server"
         (use 'lamina.core 'aleph.tcp 'gloss.core)

         (defn handler [ch client-info]
           (receive-all ch
                        #(enqueue ch (str "You said " %))))

         (start-tcp-server handler {:port 10000, :frame (string :utf-8 :delimiters ["\r\n"])}))

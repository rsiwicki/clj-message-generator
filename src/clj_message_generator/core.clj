(ns clj-message-generator.core
  (:require [langohr.core    :as rmq]
          [langohr.channel :as lch]
          [langohr.queue :as lq]
          [langohr.consumers :as lc]
          [langohr.basic :as lb]))

(def ^{:const true}
    default-exchange-name "")

(def thinktime 700)

(defn -main
    [& args]
    (print "starting emitter with args " args)
    (let [conn (rmq/connect)
                  ch (lch/open conn)
                  qname (nth args 0)]
          (println (format "[main] Connected. Channel id: %d" (.getChannelNumber ch)))
          (lq/declare ch qname :exclusive false :durable true :auto-delete false)
          (loop [i 10]
           (when (> i 0) 
            (do (lb/publish ch default-exchange-name qname "interesting" :content-type "text/plain" :type "storm.events")
            (println "emit")
            (Thread/sleep thinktime)
           )
          (recur (- i 1))))
          (println "[main] Disconnecting...")
          (rmq/close ch)
          (rmq/close conn)))


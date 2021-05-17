(ns further-testing.core
  (:require [clojure.string :as str])
  (:require [clojure.math.combinatorics :as combo])
  )

(def stegregex "[qwertasdfzxcvQWERTASDFZXCV]")

(def words
  (->> (slurp "./resources/google-10000-english-usa.txt") ; (slurp "/usr/share/dict/words")
       str/split-lines
       (map str/trim)
       set
       (filter #(= 5 (count %)))
       (filter #(Character/isLowerCase (first %)))))

(def animals
  (->> (slurp "./resources/animals.txt")
       str/split-lines
       (map str/trim)
       set
       (filter #(= 5 (count %)))))

(def steg
  (->> (slurp "./resources/steg.txt")
       str/split-lines
       (map str/trim)
       set
       )
  )

(defn score-word
  "Scores the word according to the Stegosaurus algorithm, or with a supplied regex."
  ([word re]
   (reduce (fn [acc cur]
             (if (re-find (re-pattern re) (str (nth word cur))) ; Check if the letter is in the regex.
               (+ acc (int (Math/pow 2 (- 4 cur)))); Add in the power of two, if it matches.
               acc)) ; Otherwise, keep the accumulator the same.
           0 ; Reduction starting with 0.
           (range 5))) ; Over the course of all 5 letters.
  ([word]
    (score-word word "[qwertasdfzxcvbQWERTASDFZXCVB]")))

(defn is32? "Checks to see if a particular wordlist has a perfect score of 32."
  [re wordlist]
  (reduce (fn [acc cur]
            (and acc (seq (filter #(= cur (score-word % (re-pattern re))) wordlist))))
          true
          (range 32)))

(defn regex-score
    "Takes in a regex and a wordlist.
    Provides a score. This is how many hits for the wordlist & regex."
                                        ; (regex-score "[qwertasdfzxcvb]" animals)
                                        ; => 26
  [re wordlist]
  (reduce (fn [acc cur]
            (if (seq (filter #(= cur (score-word % (re-pattern re))) wordlist))
              ; "If there are some in the wordlist matching the score number... then inc acc."
              (inc acc)
              acc))
          0
          (range 32)))

(def alpha-combo
  (as-> "abcdefghijklmnopqrstuvwxyz" $
        (str/split $ #"")                                 ; whole list split up individually
        (combo/combinations $ 13)                         ; first = ("a" "b" "c" "d" "e" "f" "g" "h" "i" "j" "k" "l" "m")
        (map #(str "[" (apply str %) "]") $)              ; ("[abcdefghijklm]" "[abcdefghijkln]") etc.
        (filter #(is32? % animals) $)))

(defn all-english-words-sorted []
  (loop [x 0]
    (if (= x 32)
      nil
      (recur
       (do
         (println x ": " (filter #(= x (score-word % (re-pattern stegregex))) words))
         (inc x))))))

(comment

  (take 5 words)
  (count words)
  (rand-nth (vec words))

  (sort words)                                              ; Alphabetical order

  (def words=0
    (filter #(= 0 (score-word %)) words)
    )
  (print words=0)

  (def bible
    (->> (slurp "./resources/biblical-names.txt")
         str/split-lines
         (map str/trim)
         set
         (filter #(= 5 (count %)))
         )
    )


  (defn count-letters "Count the number of letters in a word." [word letter]
    (count (re-seq (re-pattern letter) word))
    )

(count-letters "abcde" "c")


  (distinct (reduce str animals))                           ; => (\c \r \a \n \e \p \d \t \i \g \v \m \o \u \s \h \y \q \l \k \b \w \z \f)
  (frequencies (reduce str animals))


  (def alpha-combo (combo/combinations (str/split "abcdefghijklmnopqrstuvwxyz" #"") 13))

  (take 3 alpha-combo)
  (first alpha-combo)                                       ; => ("a" "b" "c" "d" "e" "f" "g" "h" "i" "j" "k" "l" "m")

  (str (first alpha-combo))                                 ; ???? nope.
  (->>
    alpha-combo
    first                                                   ; => ("a" "b" "c" "d" "e" "f" "g" "h" "i" "j" "k" "l" "m")
    (apply str)                                             ; => "abcdefghijklm"
    )


  (regex-score "[adefghjlpsuvx]" animals)                   ; success!!!


  (regex-score stegregex steg)

  (regex-score stegregex steg)
  (def stegregex "[qwertasdfzxcvQWERTASDFZXCV]")



  (> (count (filter #(= 4 (score-word % (re-pattern stegregex))) steg)) 0)

  (is31? stegregex steg)                                    ; true
  (is31? stegregex animals)                                 ; false



  (loop [x 0]
    (if (= x 32)
      nil
      (recur
        (do
          (println x ": " (filter #(= x (score-word % "[adefghjlpsuvx]")) animals))
          (inc x)))
      )
    )                                                       ; This gives a word list for animals!

  ; Perfect animal regexes
  ; ([adefghjlpsuvx] [adefghjlpsuvz] [adefghlpsuvxz] [bcijkmnoqrtwy] [bcikmnoqrtwxy] [bcikmnoqrtwyz]))

  ;===========================================================





(map (fn [re] {:re re :sc (regex-score re animals)}) ["[abcdefghijklm]" "[abcdefghijkln]"])



  ;============================================================================

  (regex-score
    (brack-re
      (->>
        alpha-combo
        first                                               ; => ("a" "b" "c" "d" "e" "f" "g" "h" "i" "j" "k" "l" "m")
        (apply str)                                         ; => "abcdefghijklm"
        )
      )
    animals
    )                                                       ; => 19 (for the first "abcdefghijklm")


; (= (apply str (__ "Leeeeeerrroyyy")) "Leroy")

  (defn compress [& xs]
    (reduce #(when (not= %1 %2) %1) '() xs )
    )

  ;(= (__ [1 2 3] [:a :b :c]) '(1 :a 2 :b 3 :c))
  (defn inty [s1 s2]
    (when (and (first s1) (first s2))
      (flatten (conj [] (first s1) (first s2)
                     (when (and (rest s1) (rest s2)) (inty (rest s1) (rest s2)))))))


(defn inty2 [s1 s2] (flatten (map vector s1 s2)))

(defn inty3 [s1 s2] ( mapcat vector s1 s2))

  (defn inty4 [s1 s2] ((partial mapcat vector) s1 s2))
  (inty4 [1 2 3] [:a :b :c])

  (defn compr [[x & zs] ]
    (concat (when-not (= x (first zs)) [x]) (when zs (compr zs))))

  (defn dupe [& [farg & rargs :as args]]
    (when (seq args)
      (cons farg (cons farg (apply dupe rargs)))))
(defn score-word
  ([word re]
   (loop [x 0 acc 0]
     (if (= x (count word))
       acc
       (recur
         (inc x)
         (+ acc
            (if (check-letter? (str (nth word x)) re)
              (int (Math/pow 2 (- 4 x)))
              0))))))
  ([word]
   (score-word word "[qwertasdfzxcvbQWERTASDFZXCVB]")))

(defn score-word2
  "Second attempt at score-word, to create a more elegant algorithm."
  ([word re]
   (reduce +
           (for [pos (range (count word))
                :let [score (int (Math/pow 2 (- 4 pos)))]
                :when (re-find (re-pattern re) (str (nth word pos)))]
             score)))

             ; This for loop will create a series of powers of 2, but only keep them
             ; when the letter is matched within the regex. Essentially it does the
             ; same thing as in the original score-word defn. However, the elegance
             ; comes at a small cost.
             ;
             ; (time (dotimes [_ 100000] (score-word "party")))
             ; "Elapsed time: 533.07543 msec
             ;
             ;(time (dotimes [_ 100000] (score-word2 "party")))
             ; "Elapsed time: 597.83245 msecs"


  ([word]
    (score-word2 word "[qwertasdfzxcvbQWERTASDFZXCVB]")))

(defn score-word3
  "Third attempt at score-word, to create a more elegant algorithm."
  ([word re]
   (reduce (fn [acc cur]
             (if (re-find (re-pattern re) (str (nth word cur))) ; Check if the letter is in the regex.
               (+ acc (int (Math/pow 2 (- 4 cur)))); Add in the power of two, if it matches.
               acc)) ; Otherwise, keep the accumulator the same.
           0 ; Reduction starting with 0.
           (range 5)) ; Over the course of all 5 letters.
  ([word]
    (score-word3 word "[qwertasdfzxcvbQWERTASDFZXCVB]")))

  ; This version is both elegant and efficient!
  ; further-testing.core> (time (dotimes [_ 100000] (score-word3 "party")))
  ; "Elapsed time: 506.824872 msecs"


(defn check-letter
  ([letter]
   (check-letter? letter "[qwertasdfgzxcv]"))

  ([letter re]
   (check-letter? letter re)))

(defn regex-score ; old version. New version is more than 2x faster.
                    ;  (time (dotimes [_ 1000] (regex-score "[adefghjlpsuvx]" animals)))
                    ;      "Elapsed time: 2877.598247 msecs"
                    ;  (time (dotimes [_ 1000] (regex-score2 "[adefghjlpsuvx]" animals)))
                    ;      "Elapsed time: 1112.193068 msecs"

    "Takes in a regex and a wordlist.
    Provides a score. This is how many hits for the wordlist & regex."
  [re wordlist]
                                        ; (regex-score "[qwertasdfzxcvb]" animals)
                                        ; => 26

  (loop [x 0 acc 0]
    (if (= x 32)
      acc
      (recur
       (+ x 1)
       (if (> (count (filter #(= x (score-word % (re-pattern re))) wordlist)) 0)
         (+ acc 1)
         acc)))))

(defn check-letter?
  ([letter]
   (check-letter? letter "[qwertasdfgzxcv]"))

  ([letter re]
   (if (nil? re)
     (re-find #"[qwertasdfgzxcv]" letter)
     (re-find (re-pattern re) letter)
     ))
  )

(defn is31? "Checks to see if a particular wordlist has a perfect score of 31"
  [re wordlist]
  ; Deprecated. Reimplemented by is32?
  (loop [i 0 f false]
    (if (or (= i 32) f)
      (and (not f) (= i 32))
      (recur
        (inc i)
        (= (count (filter #(= i (score-word % (re-pattern re))) wordlist)) 0)))))

  (__ 0 [:a :b :c]) {:a 0 :b 0 :c 0})
;
;end comment
)

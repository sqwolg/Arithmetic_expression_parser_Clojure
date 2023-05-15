(load-file "proto.clj")

(defn constant [value]
  (fn [_] value))

(defn variable [name]
  #(get % name))

(defn factory [operation]
  (fn [first second]
    (fn [vars] (operation (first vars) (second vars)))))

(defn negate [first] (fn [value] (- (first value))))
(defn arcTan [first] (fn [value] (Math/atan (double (first value)))))
(def arcTan2 (factory (fn [first second] (Math/atan2 (double first) (double second)))))
(def add (factory +))
(def multiply (factory *))
(def subtract (factory -))
(def divide (factory (fn [first second] (/ (double first) (double second)))))

(def ew-op {'negate negate
            '+ add
            '- subtract
            '* multiply
            '/ divide
            'atan arcTan
            'atan2 arcTan2})

(defn parses [expression] (cond
                            (number? expression) (constant expression)
                            (symbol? expression) (variable (name expression))
                            :else (apply (get ew-op (first expression)) (mapv parses (rest expression)))))

(defn parseFunction [expression] (parses (read-string expression)))

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

(def evaluate (method :evaluate))
(def toString (method :toString))

(def proto-Var-Const
  {:toString (fn [this] (str this))})

(def proto-Const
  (merge proto-Var-Const
         {:evaluate (fn [this & _] (:value this))
          :toString (fn [this] (str (:value this)))}))

(def proto-Var
  (merge proto-Var-Const
         {:evaluate (fn [this vars] (vars (:name this)))
          :toString (fn [this] (str (:name this)))}))

(defn Constant [number]
  {:prototype proto-Const :value number})

(defn Variable [name]
  {:prototype proto-Var :name name})

(def op (let [first (field :first)
              second (field :second)
              operation (field :operation)
              symbol (field :symbol)]
          {:toString (fn [this] (str "(" (symbol this) " " (toString (first this))
                                     (if (second this) (str " " (toString (second this))) "") ")"))
           :evaluate (fn [this vars] (if (second this)
                                       ((operation this) (evaluate (first this) vars) (evaluate (second this) vars))
                                       ((operation this) (evaluate (first this) vars))))}))

(defn create-op [symbol operation]
  (fn ([first second]
       {:prototype {:prototype op
                    :symbol    symbol
                    :operation operation}
        :first  first
        :second  second})
    ([operand]
     {:prototype {:prototype op
                  :symbol    symbol
                  :operation operation}
      :first  operand})))

(def Add (create-op "+" +))
(def Subtract (create-op "-" -))
(def Multiply (create-op "*" *))
(def Divide (create-op "/" (fn [first second] (/ (double first) (double second)))))
(def Negate (create-op "negate" -))
(def Sinh (create-op "sinh" (fn [f] (Math/sinh f))))
(def Cosh (create-op "cosh" (fn [f] (Math/cosh f))))

(def ew-op-obj {'negate Negate
                '+      Add
                '-      Subtract
                '*      Multiply
                '/      Divide
                'sinh   Sinh
                'cosh  Cosh})

(defn parseFunctionObject [expression] (cond
                                         (number? expression) (Constant expression)
                                         (symbol? expression) (Variable (name expression))
                                         :else (apply (get ew-op-obj (first expression)) (mapv parseFunctionObject (rest expression)))))

(defn parseObject [expression] (parseFunctionObject (read-string expression)))


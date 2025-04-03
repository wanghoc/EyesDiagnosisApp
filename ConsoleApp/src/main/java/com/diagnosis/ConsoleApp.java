package com.diagnosis;

import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Scanner;

import weka.classifiers.Classifier;
import weka.core.Attribute;
import weka.core.DenseInstance;
import weka.core.Instance;
import weka.core.Instances;
import weka.core.SerializationHelper;
import weka.core.converters.ConverterUtils;

public class ConsoleApp {
    public static void main(String[] args) throws Exception {
        Run2();
    }

    public static void Run1() {
        DecisionTree dt = new DecisionTree();
        dt.Run();
    }

    public static void Run2() throws Exception {
        Classifier model = loadModel("SharedLibrary/models/random_forest_model.rf");
        // Classifier model = loadModel("SharedLibrary/models/decision_tree_model.j48");
        Instances attributesStructure = loadFeatureStructure("SharedLibrary/models/features.arff");
        Instance instance = getUserInputInstance(attributesStructure);

        // Lấy phân phối xác suất cho từng lớp
        double[] probabilities = model.distributionForInstance(instance);

        // Tạo danh sách các cặp (label, probability)
        ArrayList<Prediction> predictions = new ArrayList<>();
        for (int i = 0; i < probabilities.length; i++) {
            String classLabel = attributesStructure.classAttribute().value(i);
            predictions.add(new Prediction(classLabel, probabilities[i]));
        }

        // Sắp xếp theo xác suất giảm dần
        predictions.sort((p1, p2) -> Double.compare(p2.probability, p1.probability));

        // Hiển thị 3 bệnh có xác suất cao nhất
        System.out.println("3 bệnh có xác suất cao nhất:");
        for (int i = 0; i < Math.min(3, predictions.size()); i++) {
            Prediction prediction = predictions.get(i);
            System.out.printf("%d. %s (%.2f%%)\n", i + 1, prediction.classLabel, prediction.probability * 100);
        }
    }

    public static Classifier loadModel(String modelPath) throws Exception {
        return (Classifier) SerializationHelper.read(modelPath);
    }

    public static Instances loadFeatureStructure(String filePath) throws Exception {
        ConverterUtils.DataSource source = new ConverterUtils.DataSource(filePath);
        Instances structure = source.getStructure();
        structure.setClassIndex(0);
        return structure;
    }

    public static Instance getUserInputInstance(Instances structure) {
        Scanner scanner = new Scanner(System.in);
        ArrayList<Double> values = new ArrayList();
        System.out.println("Vui lòng nhập giá trị cho các thuộc tính sau đây:");

        for (int i = 0; i < structure.numAttributes(); ++i) {
            if (i == structure.classIndex()) {
                values.add(0.0);
            } else {
                PrintStream var10000 = System.out;
                Attribute var10001 = structure.attribute(i);
                var10000.print(var10001.name() + ": ");
                if (structure.attribute(i).isNumeric()) {
                    double val = scanner.nextDouble();
                    values.add(val);
                } else if (structure.attribute(i).isNominal()) {
                    var10000 = System.out;
                    var10001 = structure.attribute(i);
                    var10000.print("Chọn trong khoảng " + var10001.enumerateValues().toString() + ": ");
                    String val = scanner.next();

                    int index;
                    for (index = structure.attribute(i).indexOfValue(val); index == -1; index = structure.attribute(i)
                            .indexOfValue(val)) {
                        System.out.print("Đầu vào không hợp lệ. Thử lại: ");
                        val = scanner.next();
                    }

                    values.add((double) index);
                }
            }
        }

        Instance instance = new DenseInstance(1.0, values.stream().mapToDouble(Double::doubleValue).toArray());
        instance.setDataset(structure);
        return instance;
    }

    private static class Prediction {
        String classLabel;
        double probability;

        public Prediction(String classLabel, double probability) {
            this.classLabel = classLabel;
            this.probability = probability;
        }
    }
}
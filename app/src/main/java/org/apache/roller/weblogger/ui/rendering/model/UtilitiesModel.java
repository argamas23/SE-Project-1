package org.apache.roller.weblogger.ui.rendering.model;

public class UtilitiesModel {

    private static final int MAXIMUM_LENGTH = 50;
    private static final int DEFAULT_VALUE = 10;

    private String name;
    private int value;

    public UtilitiesModel(String name, int value) {
        this.name = name;
        this.value = value;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    public boolean isWithinLimits(String input) {
        return input.length() <= MAXIMUM_LENGTH;
    }

    public int getDefaultValue() {
        return DEFAULT_VALUE;
    }

    public String formatString(String input) {
        if (input.length() > MAXIMUM_LENGTH) {
            return input.substring(0, MAXIMUM_LENGTH);
        } else {
            return input;
        }
    }

    public void performOperation(int operation) {
        OperationFactory operationFactory = new OperationFactory();
        Operation operationImpl = operationFactory.getOperation(operation);
        operationImpl.execute(this);
    }

    private static class OperationFactory {
        public Operation getOperation(int operation) {
            return switch (operation) {
                case 1 -> new Operation1();
                case 2 -> new Operation2();
                case 3 -> new Operation3();
                default -> throw new UnsupportedOperationException("Unsupported operation");
            };
        }
    }

    private interface Operation {
        void execute(UtilitiesModel model);
    }

    private static class Operation1 implements Operation {
        @Override
        public void execute(UtilitiesModel model) {
            // Implement operation 1
            model.setValue(model.getValue() + 5);
        }
    }

    private static class Operation2 implements Operation {
        @Override
        public void execute(UtilitiesModel model) {
            // Implement operation 2
            model.setValue(model.getValue() - 3);
        }
    }

    private static class Operation3 implements Operation {
        @Override
        public void execute(UtilitiesModel model) {
            // Implement operation 3
            model.setValue(model.getValue() * 2);
        }
    }
}
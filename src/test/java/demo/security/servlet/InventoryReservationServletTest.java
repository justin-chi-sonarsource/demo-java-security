package demo.security.servlet;

public class InventoryReservationServletTest {

    @org.junit.Test
    public void doPost_withNegativeQuantityReturnsBadRequest() throws Exception {
        InventoryReservationServlet servlet = new InventoryReservationServlet();
        RequestHandler requestHandler = new RequestHandler();
        ResponseHandler responseHandler = new ResponseHandler();
        javax.servlet.http.HttpServletRequest request = (javax.servlet.http.HttpServletRequest) java.lang.reflect.Proxy.newProxyInstance(
            javax.servlet.http.HttpServletRequest.class.getClassLoader(),
            new Class<?>[] { javax.servlet.http.HttpServletRequest.class },
            requestHandler);
        javax.servlet.http.HttpServletResponse response = (javax.servlet.http.HttpServletResponse) java.lang.reflect.Proxy.newProxyInstance(
            javax.servlet.http.HttpServletResponse.class.getClassLoader(),
            new Class<?>[] { javax.servlet.http.HttpServletResponse.class },
            responseHandler);

        servlet.doPost(request, response);

        org.junit.Assert.assertEquals(javax.servlet.http.HttpServletResponse.SC_BAD_REQUEST, responseHandler.errorCode);
        org.junit.Assert.assertEquals("Quantity must be a positive integer", responseHandler.errorMessage);
        org.junit.Assert.assertFalse(responseHandler.writerCalled);
    }

    private static class RequestHandler implements java.lang.reflect.InvocationHandler {

        @Override
        public Object invoke(Object proxy, java.lang.reflect.Method method, Object[] args) {
            if ("getParameter".equals(method.getName())) {
                if ("sku".equals(args[0])) {
                    return "limited-edition-item";
                }
                if ("quantity".equals(args[0])) {
                    return "-1";
                }
            }
            return defaultValue(method.getReturnType());
        }
    }

    private static class ResponseHandler implements java.lang.reflect.InvocationHandler {

        private int errorCode;
        private String errorMessage;
        private boolean writerCalled;

        @Override
        public Object invoke(Object proxy, java.lang.reflect.Method method, Object[] args) {
            if ("sendError".equals(method.getName())) {
                errorCode = (Integer) args[0];
                errorMessage = (String) args[1];
                return null;
            }
            if ("getWriter".equals(method.getName())) {
                writerCalled = true;
                return new java.io.PrintWriter(new java.io.StringWriter());
            }
            return defaultValue(method.getReturnType());
        }
    }

    private static Object defaultValue(Class<?> returnType) {
        if (!returnType.isPrimitive() || Void.TYPE.equals(returnType)) {
            return null;
        }
        if (Boolean.TYPE.equals(returnType)) {
            return false;
        }
        if (Character.TYPE.equals(returnType)) {
            return '\0';
        }
        return 0;
    }
}

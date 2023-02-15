package io.github.h800572003.sql.generate.body;

import com.helger.jcodemodel.JCodeModel;
import com.helger.jcodemodel.JInvocation;
import io.github.h800572003.sql.SqlBuilder;
import io.github.h800572003.sql.generate.GenerateWord;
import io.github.h800572003.sql.generate.IGenerateContext;
import io.github.h800572003.sql.select.SelectBuilder;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.util.HashMap;
import java.util.Map;

@Slf4j
public class AddBodyWriteCode implements ICodeWrite {


    private final String SPLIT_PATTEN=",(?=\\w|\\s)";

    public static final String SELECT = "SELECT";
    public static final String FROM = "FROM";
    public static final String WHERE = "WHERE";
    public static final String UNION = "UNION";
    private static final String CREATE_SELECT = "createSelect";
    private static final String INNER_FROM = "innerFrom";//
    public static final String END = ";";
    public static final String GROUP = "GROUP";
    public static final String ORDER = "ORDER";


    private Map<String, StatusListener> statusForwardListeners = new HashMap<>();
    private Map<String, StatusListener> statusReverseListeners = new HashMap<>();

    public AddBodyWriteCode() {
        //順向
        this.addForwardStatus(SELECT, new SelectStatusListener());
        this.addForwardStatus(FROM, new FromStatusListener());
        this.addForwardStatus(WHERE, new WhereStatusListener());
        this.addForwardStatus(ORDER, new OrderStatusListener());

        //逆向
        this.addReverseStatus(SELECT, new SelectReverseStatusListener());
    }


    interface StatusListener {
        void update(String oldStatus, String newStatus, IGenerateContext context);
    }

    protected void addForwardStatus(String oldStatus, StatusListener statusListener) {
        statusForwardListeners.put(oldStatus, statusListener);
    }

    protected void addReverseStatus(String newStatus, StatusListener statusListener) {
        statusReverseListeners.put(newStatus, statusListener);
    }


    public void update(String oldStatus, String newStatus, IGenerateContext context) {
        if (!StringUtils.equalsIgnoreCase(oldStatus, newStatus)) {
            StatusListener statusListener = statusForwardListeners.get(oldStatus);//順向
            if (statusListener != null) {
                statusListener.update(oldStatus, newStatus, context);
            }
            StatusListener reverse = statusReverseListeners.get(newStatus);//逆向
            if (reverse != null) {
                reverse.update(oldStatus, newStatus, context);
            }
        }
    }

    @Override
    public String generate(String value, IGenerateContext context) {
        JInvocation jInvocation = context.get(MapperCodes.INVOKE, JInvocation.class);


        log.info("key:{}", value);

        if (isKeyWord(value.toUpperCase())) {
            updateStatus(value, context);
        }
        String status = context.get(MapperCodes.STATUS, String.class);
        if (StringUtils.equalsIgnoreCase(status, SELECT)) {
            addSelect(value, context);
        } else if (StringUtils.equalsIgnoreCase(status, FROM)) {
            addFrom(value, context);
        } else if (StringUtils.equalsIgnoreCase(status, WHERE)) {
            addWhere(value, context);
        } else if (StringUtils.equalsIgnoreCase(status, END)) {
            anddEnd(context);
        } else if (StringUtils.equalsIgnoreCase(status, ORDER)) {
            addOrder(value, context);
        } else if (StringUtils.equalsIgnoreCase(status, GROUP)) {
            addGROUP(value, context);
        } else {
            addOther(value, context);
        }
        return StringUtils.EMPTY;
    }

    private void addGROUP(String value, IGenerateContext context) {
        JInvocation jInvocation = context.get(MapperCodes.INNER_SELECT, JInvocation.class);
        if (StringUtils.equalsIgnoreCase(value.toUpperCase(), GROUP)) {
            context.put(MapperCodes.INNER_SELECT, jInvocation.invoke("groupBy"));
        } else if (StringUtils.equalsIgnoreCase(value.toUpperCase(), "BY")) {

        } else {
            String[] values = value.split(SPLIT_PATTEN);
            for (String v : values) {
                JCodeModel model = context.getModel();
                jInvocation = jInvocation.arg(v.trim());
            }
            context.put(MapperCodes.INNER_SELECT, jInvocation);
        }
    }

    private void addOrder(String value, IGenerateContext context) {
        GenerateWord word = context.getWord();

        JInvocation jInvocation = context.get(MapperCodes.INNER_SELECT, JInvocation.class);
        if (StringUtils.equalsIgnoreCase(value.toUpperCase(), ORDER)) {
            context.put(MapperCodes.INNER_SELECT, jInvocation.invoke("createOrderBy"));
        } else if (StringUtils.equalsIgnoreCase(value.toUpperCase(), "BY")) {
            //not do
        } else if (StringUtils.equalsIgnoreCase(value.toUpperCase(), "DESC")) {
            context.put(MapperCodes.INNER_SELECT, jInvocation.invoke("isDesc"));
        } else if (StringUtils.equalsIgnoreCase(value.toUpperCase(), "ASC")) {
            //not do
        } else {
            String[] values = value.split(SPLIT_PATTEN);
            for (String v : values) {
                JCodeModel model = context.getModel();
                JInvocation write = model.ref(SqlBuilder.class).staticInvoke("write").arg(v.trim());
                jInvocation = jInvocation.invoke("add").arg(write);
            }
            context.put(MapperCodes.INNER_SELECT,jInvocation);

        }
    }

    private  void addSelect(String value, IGenerateContext context) {
        if (value.toUpperCase().matches("(SELECT)")) {
            addSelectNew(context);
        } else {
            adddSelectAdd(value, context);
        }
    }

    private  void addFrom(String value, IGenerateContext context) {
        if (value.toUpperCase().matches("(FROM)")) {
        } else {
            addFromValue(value, context);
        }
    }

    private void addOther(String value, IGenerateContext context) {
        JInvocation innerSelect = context.get(MapperCodes.INNER_SELECT, JInvocation.class);//

        context.put(MapperCodes.INNER_SELECT, innerSelect.invoke("appendWithSpace").arg(value));//
    }

    private static void anddEnd(IGenerateContext context) {
        JInvocation innerSelect = context.get(MapperCodes.INNER_SELECT, JInvocation.class);//
        JInvocation invocation = context.get(MapperCodes.INVOKE, JInvocation.class);//
        if (innerSelect != null) {
            context.put(MapperCodes.INVOKE, invocation.invoke("addWithSpace").arg(innerSelect));//

        }
    }

    enum WhereStatus {
        START,//開始
        AND,//AND
        OR,//OR
    }

    private static void addWhere(String value, IGenerateContext context) {

        JCodeModel model = context.getModel();
        JInvocation selectInvoke = context.get(MapperCodes.INNER_SELECT, JInvocation.class);

        JInvocation innerWhere = context.get(MapperCodes.INNER_WHERE, JInvocation.class);
        if (StringUtils.equalsIgnoreCase(value, "AND")) {
            context.put(MapperCodes.INNER_SELECT, selectInvoke.arg(innerWhere).invoke("and"));
            context.put(MapperCodes.INNER_WHERE, null);
            context.put(MapperCodes.INNER_WHERE_STATUS, WhereStatus.AND);
        } else if (StringUtils.equalsIgnoreCase(value, "OR")) {
            context.put(MapperCodes.INNER_SELECT, selectInvoke.arg(innerWhere).invoke("or"));
            context.put(MapperCodes.INNER_WHERE, null);
            context.put(MapperCodes.INNER_WHERE_STATUS, WhereStatus.OR);
        } else if (StringUtils.equalsIgnoreCase(value.toUpperCase(), "WHERE")) {
            context.put(MapperCodes.INNER_SELECT, selectInvoke.invoke("createWhere"));
            context.put(MapperCodes.INNER_WHERE_STATUS, WhereStatus.START);
        } else {
            WhereStatus status = context.get(MapperCodes.INNER_WHERE_STATUS, WhereStatus.class);
            if (innerWhere == null) {
                innerWhere = model.ref(SqlBuilder.class).staticInvoke("write");
            }
            innerWhere = innerWhere.arg(value);
            context.put(MapperCodes.INNER_WHERE, innerWhere);


        }


    }

    private void updateStatus(String value, IGenerateContext context) {
        final String newStatus = value.toUpperCase();
        final String oldStatus = context.get(MapperCodes.STATUS, String.class);
        this.update(oldStatus, newStatus, context);//通知狀態變更
        context.put(MapperCodes.STATUS, newStatus);//
    }

    private  void addFromValue(String value, IGenerateContext context) {
        JInvocation innerFrom = context.get(MapperCodes.INNER_FROM, JInvocation.class);//
        if (innerFrom == null) {
            JCodeModel model = context.getModel();
            innerFrom = model.ref(SqlBuilder.class).staticInvoke("write");
            innerFrom.arg(value);
            context.put(MapperCodes.INNER_FROM, innerFrom);
        }
    }

    private  void adddSelectAdd(String value, IGenerateContext context) {
        JInvocation selectInvoke = context.get(MapperCodes.INNER_SELECT, JInvocation.class);//
        final String[] values = value.split(SPLIT_PATTEN);
        for (String v : values) {
            selectInvoke = selectInvoke.invoke("add").arg(v.trim());
        }
        context.put(MapperCodes.INNER_SELECT, selectInvoke);//

    }

    private static void addSelectNew(IGenerateContext context) {
        JInvocation selectInvoke = context.getModel().ref(SelectBuilder.class).staticInvoke("newSelect").invoke(CREATE_SELECT);
        context.put(MapperCodes.INNER_SELECT, selectInvoke);//
    }

    private boolean isKeyWord(String key) {
        return key.matches("(SELECT|FROM|JOIN|UNION|WHERE|ORDER|GROUP|;)");
    }


    private class WhereStatusListener implements StatusListener {
        @Override
        public void update(String oldStatus, String newStatus, IGenerateContext context) {


            JInvocation innerWhere = context.get(MapperCodes.INNER_WHERE, JInvocation.class);//
            JInvocation selectInvoke = context.get(MapperCodes.INNER_SELECT, JInvocation.class);//
            context.put(MapperCodes.INNER_SELECT, selectInvoke.arg(innerWhere).invoke("back"));
            context.put(MapperCodes.INNER_WHERE, null);
        }
    }

    class FromStatusListener implements StatusListener {

        @Override
        public void update(String oldStatus, String newStatus, IGenerateContext context) {

            JInvocation innerFrom = context.get(MapperCodes.INNER_FROM, JInvocation.class);//
            JInvocation selectInvoke = context.get(MapperCodes.INNER_SELECT, JInvocation.class);//
            context.put(MapperCodes.INNER_SELECT, selectInvoke.invoke("from").arg(innerFrom));
            context.put(MapperCodes.INNER_FROM, null);

        }
    }

    class SelectStatusListener implements StatusListener {
        @Override
        public void update(String oldStatus, String newStatus, IGenerateContext context) {

            JInvocation selectInvoke = context.get(MapperCodes.INNER_SELECT, JInvocation.class);//
            if (selectInvoke != null) {
                context.put(MapperCodes.INNER_SELECT, selectInvoke.invoke("back"));
            }


        }


    }

    private class SelectReverseStatusListener implements StatusListener {
        @Override
        public void update(String oldStatus, String newStatus, IGenerateContext context) {
            if (StringUtils.isNotBlank(oldStatus)) {
                JInvocation innerSelect = context.get(MapperCodes.INNER_SELECT, JInvocation.class);//
                JInvocation invocation = context.get(MapperCodes.INVOKE, JInvocation.class);//
                if (innerSelect != null) {
                    context.put(MapperCodes.INVOKE, invocation.invoke("addWithSpace").arg(innerSelect));//
                    context.put(MapperCodes.INNER_SELECT, null);
                }

            }
        }
    }


    private class OrderStatusListener implements StatusListener {
        @Override
        public void update(String oldStatus, String newStatus, IGenerateContext context) {
//            JInvocation inner = context.get(MapperCodes.INNER_ORDER, JInvocation.class);//
            JInvocation selectInvoke = context.get(MapperCodes.INNER_SELECT, JInvocation.class);//
            context.put(MapperCodes.INNER_SELECT, selectInvoke.invoke("back"));

        }
    }
}

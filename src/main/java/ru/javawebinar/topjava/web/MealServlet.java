package ru.javawebinar.topjava.web;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.util.exception.NotFoundException;
import ru.javawebinar.topjava.web.meal.MealRestController;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Objects;


public class MealServlet extends HttpServlet {
    private static final Logger LOG = LoggerFactory.getLogger(MealServlet.class);


    private MealRestController restController;


    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        try (ConfigurableApplicationContext appCtx = new ClassPathXmlApplicationContext("spring/spring-app.xml")) {
            restController = appCtx.getBean(MealRestController.class);
        }

    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        String id = request.getParameter("id");
        String action = request.getParameter("action");
        try {
        if (action != null)
        {
            Meal meal = new Meal(id.isEmpty() ? null : Integer.valueOf(id),
                    LocalDateTime.parse(request.getParameter("dateTime")),
                    request.getParameter("description"),
                    Integer.valueOf(request.getParameter("calories")));
            LOG.info(meal.isNew() ? "Create {}" : "Update {}", meal);
            restController.save(meal);
            response.sendRedirect("meals");
        }
        else
        {
            String dateFrom = request.getParameter("dateFrom");
            String dateTo = request.getParameter("dateTo");
            String timeFrom = request.getParameter("timeFrom");
            String timeTo = request.getParameter("timeTo");
            request.setAttribute("mealList", restController.getBetween(dateFrom, dateTo, timeFrom, timeTo));
            request.getRequestDispatcher("mealList.jsp").forward(request, response);
        }
        }catch (NotFoundException e)
        {
            request.setAttribute("errorMsg", e.getMessage());
            request.getRequestDispatcher("error.jsp").forward(request, response);

        }

    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getParameter("action");

        try {
            if (action == null) {
                LOG.info("getAll");
                request.setAttribute("mealList",restController.getAll());
                request.getRequestDispatcher("/mealList.jsp").forward(request   , response);

            } else if ("delete".equals(action)) {
                int id = getId(request);
                LOG.info("Delete {}", id);
                restController.delete(id);
                response.sendRedirect("meals");

            } else if ("create".equals(action) || "update".equals(action)) {
                final Meal meal = action.equals("create") ?
                        new Meal(LocalDateTime.now().withNano(0).withSecond(0), "", 1000) :
                        restController.get(getId(request));
                request.setAttribute("meal", meal);
                request.getRequestDispatcher("mealEdit.jsp").forward(request, response);
            }
        }catch (NotFoundException e)
        {
            request.setAttribute("errorMsg", e.getMessage());
            request.getRequestDispatcher("error.jsp").forward(request, response);

        }
    }

    private int getId(HttpServletRequest request) {
        String paramId = Objects.requireNonNull(request.getParameter("id"));
        return Integer.valueOf(paramId);
    }


}

package com.github.q115.goalie_android.utilsTest;

import android.support.test.InstrumentationRegistry;

import com.github.q115.goalie_android.models.Goal;
import com.github.q115.goalie_android.utils.GoalHelper;
import com.github.q115.goalie_android.utils.UserHelper;
import com.raizlabs.android.dbflow.config.FlowManager;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static test_util.DatabaseUtil.ReadDatabase;

/**
 * Created by Qi on 9/5/2017.
 */

public class GoalHelperInstrumentedTest {
    @BeforeClass
    public static void init() {
        FlowManager.init(InstrumentationRegistry.getTargetContext());
    }

    @AfterClass
    public static void teardown() {
        FlowManager.reset();
    }

    @Test
    public void initalization() throws Exception {
        GoalHelper.getInstance().initialize();
        assertNotNull(GoalHelper.getInstance().getFeeds());
        assertNotNull(GoalHelper.getInstance().getRequests());
        assertEquals(0, GoalHelper.getInstance().getFeeds().size());
        assertEquals(0, GoalHelper.getInstance().getRequests().size());
    }

    @Test
    public void goalTest() throws Exception {
        assertEquals(UserHelper.getInstance().getOwnerProfile().activieGoals.size(), 0);
        assertEquals(UserHelper.getInstance().getOwnerProfile().finishedGoals.size(), 0);

        Goal goal = new Goal("goal", Goal.GoalCompleteResult.Pending);
        GoalHelper.getInstance().addGoal(goal);
        assertEquals(UserHelper.getInstance().getOwnerProfile().activieGoals.size(), 1);
        assertEquals(UserHelper.getInstance().getOwnerProfile().finishedGoals.size(), 0);

        Goal goal2 = new Goal("goal2", Goal.GoalCompleteResult.Ongoing);
        GoalHelper.getInstance().addGoal(goal2);
        assertEquals(UserHelper.getInstance().getOwnerProfile().activieGoals.size(), 2);
        assertEquals(UserHelper.getInstance().getOwnerProfile().finishedGoals.size(), 0);

        Goal goal3 = new Goal("goal3", Goal.GoalCompleteResult.Success);
        GoalHelper.getInstance().addGoal(goal3);
        assertEquals(UserHelper.getInstance().getOwnerProfile().activieGoals.size(), 2);
        assertEquals(UserHelper.getInstance().getOwnerProfile().finishedGoals.size(), 1);

        Goal goal4 = new Goal("goal4", Goal.GoalCompleteResult.Failed);
        GoalHelper.getInstance().addGoal(goal4);
        assertEquals(UserHelper.getInstance().getOwnerProfile().activieGoals.size(), 2);
        assertEquals(UserHelper.getInstance().getOwnerProfile().finishedGoals.size(), 2);

        Goal goal5 = new Goal("goal5", Goal.GoalCompleteResult.Cancelled);
        GoalHelper.getInstance().addGoal(goal5);
        assertEquals(UserHelper.getInstance().getOwnerProfile().activieGoals.size(), 2);
        assertEquals(UserHelper.getInstance().getOwnerProfile().finishedGoals.size(), 3);

        UserHelper.getInstance().getOwnerProfile().finishedGoals.clear();
        UserHelper.getInstance().getOwnerProfile().activieGoals.clear();
        UserHelper.getInstance().getAllContacts().put("", UserHelper.getInstance().getOwnerProfile());
        ReadDatabase();
        assertEquals(UserHelper.getInstance().getOwnerProfile().activieGoals.size(), 2);
        assertEquals(UserHelper.getInstance().getOwnerProfile().finishedGoals.size(), 3);

        deleteGoal(goal2);

        assertEquals(UserHelper.getInstance().getOwnerProfile().activieGoals.size(), 1);
        modifyGoal(goal4);
        assertEquals(UserHelper.getInstance().getOwnerProfile().activieGoals.size(), 2);
    }

    private void deleteGoal(Goal goal) {
        int size = UserHelper.getInstance().getOwnerProfile().activieGoals.size()
                + UserHelper.getInstance().getOwnerProfile().finishedGoals.size();

        GoalHelper.getInstance().deleteGoal(goal.guid);

        int newSize = UserHelper.getInstance().getOwnerProfile().activieGoals.size()
                + UserHelper.getInstance().getOwnerProfile().finishedGoals.size();
        assertEquals(newSize, size - 1);
    }

    private void modifyGoal(Goal goal) {
        Goal newGoal = new Goal();
        newGoal.guid = goal.guid;
        goal.goalCompleteResult = Goal.GoalCompleteResult.Ongoing;

        GoalHelper.getInstance().modifyGoal(newGoal);
        UserHelper.getInstance().getOwnerProfile().finishedGoals.clear();
        ReadDatabase();
    }
}

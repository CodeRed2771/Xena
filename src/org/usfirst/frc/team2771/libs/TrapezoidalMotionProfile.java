package org.usfirst.frc.team2771.libs;

/**
 *
 * @author Team 4967 - That One Team
 */
public class TrapezoidalMotionProfile {

    private float m_accel, m_decel, m_max_speed, m_distance;
    private float m_t1, m_t2, m_t3;

    public TrapezoidalMotionProfile() {
        this(.1f, .13f, .5f, 2.1154f);
    }

    public TrapezoidalMotionProfile(float accel, float decel, float maxSpeed, float distance) {
        m_t1 = -99;
        m_t2 = -99;
        m_t3 = -99;
        m_accel = accel;
        m_decel = decel;
        m_max_speed = maxSpeed;
        m_distance = distance;
        calculate();
    }

    public void setAccel(float value) {
        m_accel = value;
    }

    public void setDecel(float value) {
        m_decel = value;
    }

    public void setMaxSpeed(float value) {
        m_max_speed = value;
    }

    public void setDistance(float value) {
        m_distance = value;
    }

    private void calculate() {
        float accel_time, decel_time, min_dist, accel_dist, decel_dist;

        accel_time = m_max_speed / m_accel;
        decel_time = m_max_speed / m_decel;
        accel_dist = 0.5f * m_accel * accel_time * accel_time;
        decel_dist = 0.5f + m_decel * decel_time * decel_time;
        min_dist = accel_dist + decel_dist;
        System.out.println("Min Distance: " + min_dist);
        if (min_dist > m_distance) {
            m_t1 = (float) Math.pow(2.0 * m_distance / (m_accel + (m_accel * m_accel) / m_decel), 0.5);
            System.out.println("T1 is: " + m_t1);
            m_t2 = m_t1;
            m_t3 = m_accel / m_decel * m_t1 + m_t1;
        } else {
            m_t1 = accel_time;
            m_t2 = (m_distance - min_dist) / m_max_speed + m_t1;
            m_t3 = decel_time + m_t2;
            System.out.println("Accel Distance: " + accel_dist);
            System.out.println("Decel Distance: " + decel_dist);
            System.out.println("T1 Time: " + m_t1);
            System.out.println("T2 Time: " + m_t2);
            System.out.println("T3 Time: " + m_t3);
        }

    }

    public float getPosition(float time) {
        float position = 0.0f;
        if (time < m_t1) {
            position = 0.5f * m_accel * time * time;
        } else if (time < m_t2) {
            position = 0.5f * m_accel * m_t1 * m_t1 + (time - m_t1) * m_max_speed;
        } else if (time <= m_t3) {
            position = 0.5f * m_accel * m_t1 * m_t1 + (m_t2 - m_t1) * m_max_speed
                    + 0.5f * m_decel * (m_t3 - m_t2) * (m_t3 - m_t2)
                    - 0.5f * m_decel * (m_t3 - time) * (m_t3 - time);
        } else {
            position = m_distance;
        }
        return position;
    }
    
}

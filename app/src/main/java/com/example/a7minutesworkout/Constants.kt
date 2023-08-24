package com.example.a7minutesworkout

object Constants {

    fun defaultExerciseList(): ArrayList<ExerciseModel> {
        val exerciseList = ArrayList<ExerciseModel>()

        val exercise1 = ExerciseModel (1,"Bridge Pose", R.drawable.bridge_pose,false,false )
        exerciseList.add(exercise1)

        val exercise2 = ExerciseModel (2,"Chair Pose", R.drawable.chair_pose,false,false )
        exerciseList.add(exercise2)

        val exercise3 = ExerciseModel (3,"Gate Pose", R.drawable.gate_pose,false,false )
        exerciseList.add(exercise3)

        val exercise4 = ExerciseModel (4,"Half Moon", R.drawable.half_moon,false,false )
        exerciseList.add(exercise4)

        val exercise5 = ExerciseModel (5,"Lateral Leg Lift", R.drawable.lateral_leg_lift,false,false )
        exerciseList.add(exercise5)

        val exercise6 = ExerciseModel (6,"Lord of Dance", R.drawable.lord_of_dance,false,false )
        exerciseList.add(exercise6)

        val exercise7 = ExerciseModel (7,"Lunge", R.drawable.lunge,false,false )
        exerciseList.add(exercise7)

        val exercise8 = ExerciseModel (8,"Side Plank", R.drawable.side_plank,false,false )
        exerciseList.add(exercise8)

        val exercise9 = ExerciseModel (9,"Single Leg Extension", R.drawable.single_leg_extension,false,false )
        exerciseList.add(exercise9)

        val exercise10 = ExerciseModel (10,"Single Leg Twist", R.drawable.single_leg_twist,false,false )
        exerciseList.add(exercise10)

        val exercise11 = ExerciseModel (11,"Triangle Pose", R.drawable.triangle_pose,false,false )
        exerciseList.add(exercise11)

        val exercise12 = ExerciseModel (12,"Warrior Pose", R.drawable.warrior_pose,false,false )
        exerciseList.add(exercise12)


        return exerciseList
    }

}
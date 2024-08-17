import os

DATA_FILE_PATH = "patient_records.txt"

def load_patient_records():
    if not os.path.exists(DATA_FILE_PATH):
        return {}
    with open(DATA_FILE_PATH, "r") as file:
        lines = file.readlines()
    patient_records = {}
    for line in lines:
        if ':' in line:
            patient_id, health_record = line.strip().split(':', 1)
            patient_records[patient_id] = health_record
    return patient_records

def save_patient_records(patient_records):
    with open(DATA_FILE_PATH, "w") as file:
        for patient_id, health_record in patient_records.items():
            file.write(f"{patient_id}:{health_record}\n")

def add_patient_record(patient_records, patient_id, health_record):
    patient_records[patient_id] = health_record
    save_patient_records(patient_records)
    print("Record added successfully.")

def get_patient_record(patient_records, patient_id):
    return patient_records.get(patient_id, "No record found for this ID.")

def main():
    patient_records = load_patient_records()
    while True:
        print("1. Add Record")
        print("2. View Record")
        print("3. Exit")
        user_choice = input("Choose an option: ")

        if user_choice == '1':
            patient_id = input("Enter patient ID: ")
            health_record = input("Enter health record: ")
            add_patient_record(patient_records, patient_id, health_record)
        elif user_choice == '2':
            patient_id = input("Enter patient ID: ")
            print("Health Record:", get_patient_record(patient_records, patient_id))
        elif user_choice == '3':
            print("Exiting...")
            print("Sucessfully exited")
            break
        else:
            print("Invalid option. Please try again.")

if __name__ == "__main__":
    main()
